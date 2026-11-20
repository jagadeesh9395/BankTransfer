package com.jag.account_service.service;

import com.jag.account_service.client.TransactionFeignClient;
import com.jag.account_service.dto.TransactionRequest;
import com.jag.account_service.dto.TransactionResponse;
import com.jag.account_service.entity.nosql.AccountAuditLog;
import com.jag.account_service.entity.nosql.AuditAction;
import com.jag.account_service.entity.sql.Account;
import com.jag.account_service.entity.sql.AccountStatus;
import com.jag.account_service.repository.nosql.AccountAuditRepository;
import com.jag.account_service.repository.sql.AccountRepository;
import com.jag.account_service.util.AccountCustomerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountTransferService {

    private final AccountRepository accountRepository;             // PostgreSQL (JPA)
    private final AccountAuditRepository auditRepository;          // MongoDB
    private final TransactionFeignClient transactionFeignClient;  // Feign Client
    private final AccountCustomerUtil accountCustomerUtil;

    @Transactional
    public TransactionResponse initiateTransfer(String fromAccountNum, String toAccountNum, BigDecimal amount, String performedBy) {
        if (fromAccountNum == null || toAccountNum == null) {
            throw new IllegalArgumentException("Account numbers are mandatory");
        }
        if (fromAccountNum.equals(toAccountNum)) {
            throw new IllegalArgumentException("Source and destination accounts must be different");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        // 1. Relational DB validation & balance update
        Account sourceAccount = accountRepository.findByAccountNumber(fromAccountNum)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + fromAccountNum));
        Account destinationAccount = accountRepository.findByAccountNumber(toAccountNum)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + toAccountNum));


        String customerId = accountCustomerUtil.getPerformedBy(fromAccountNum);
        String actor = (performedBy == null || performedBy.isBlank())
                ? accountCustomerUtil.getCustomerName(customerId) : performedBy.trim();

        if (!AccountStatus.ACTIVE.equals(sourceAccount.getStatus())) {
            throw new IllegalStateException("Source account is not active: " + fromAccountNum);
        }
        if (!AccountStatus.ACTIVE.equals(destinationAccount.getStatus())) {
            throw new IllegalStateException("Destination account is not active: " + toAccountNum);
        }
        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds in account: " + fromAccountNum);
        }

        LocalDateTime now = LocalDateTime.now();
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        sourceAccount.setUpdatedAt(now);
        destinationAccount.setBalance(destinationAccount.getBalance().add(amount));
        destinationAccount.setUpdatedAt(now);
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        // 2. Delegate ledger persistence to Transaction Service via OpenFeign
        String internalRef = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        TransactionRequest txRequest = new TransactionRequest(internalRef, fromAccountNum, toAccountNum, amount, "DEBIT_TRANSFER");
        TransactionResponse txResponse = transactionFeignClient.createTransaction(txRequest);

        // 3. Persist async/unstructured audit trail into MongoDB
        AccountAuditLog auditLog = AccountAuditLog.builder()
                .accountNumber(fromAccountNum)
                .action(AuditAction.FUNDS_TRANSFERRED.name())
                .performedBy(actor)
                .timestamp(Instant.now())
                .metadata(Map.of(
                        "amount", amount,
                        "reference", txResponse.transactionReference(),
                        "status", txResponse.status()
                ))
                .build();
        auditRepository.save(auditLog);

        return txResponse;
    }

    public List<TransactionResponse> getTransactionByAccountNumber(String accountNumber) {
        return transactionFeignClient.getTransactionsByAccount(accountNumber);
    }

    public Double getAccountBalanceByAccountNumber(String accountNumber) {
        return accountRepository.getAccountBalanceByAccountNumber(accountNumber);
    }
}