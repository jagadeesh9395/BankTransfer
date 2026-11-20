package com.jag.account_service.tool;

import com.jag.account_service.dto.TransactionResponse;
import com.jag.account_service.service.AccountTransferService;
import com.jag.account_service.util.AccountCustomerUtil;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountTransferTool {

    private final AccountTransferService accountTransferService;
    private final AccountCustomerUtil accountCustomerUtil;

    @Tool("Get transactions by account number")
    public List<TransactionResponse> getTransactionByAccountNumber(String accountNumber) {
        System.out.println("Tool called -> getTransactionByAccountNumber: " + accountNumber);
        return accountTransferService.getTransactionByAccountNumber(accountNumber);
    }

    @Tool("Get account balance by account number")
    public Double getAccountBalanceByAccountNumber(String accountNumber) {
        System.out.println("Tool called -> getAccountBalance: " + accountNumber);
        return accountTransferService.getAccountBalanceByAccountNumber(accountNumber);
    }


    @Tool("Initiate transfer between accounts")
    public TransactionResponse initiateTransfer(String fromAccount, String toAccount, BigDecimal amount) {
        System.out.println("Tool called -> initiateTransfer: " + fromAccount + " -> " + toAccount + " : " + amount);
        String authenticatedAccount = accountCustomerUtil.getAuthenticatedAccountNumber();
        String performedBy = accountCustomerUtil.getPerformedBy(fromAccount);
        String customerName = accountCustomerUtil.getCustomerName(performedBy);
        
        // Use authenticated user's account as source if available and matches
        if (authenticatedAccount != null && !authenticatedAccount.isBlank()) {
            fromAccount = authenticatedAccount;
            performedBy = accountCustomerUtil.getPerformedBy(fromAccount);
            customerName = accountCustomerUtil.getCustomerName(performedBy);
        }
        
        return accountTransferService.initiateTransfer(fromAccount, toAccount, amount, customerName);
    }

}
