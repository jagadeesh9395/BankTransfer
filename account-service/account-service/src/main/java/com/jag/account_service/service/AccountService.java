package com.jag.account_service.service;

import com.jag.account_service.dto.AccountRequest;
import com.jag.account_service.dto.AccountResponse;
import com.jag.account_service.entity.sql.Account;
import com.jag.account_service.entity.sql.AccountStatus;
import com.jag.account_service.repository.sql.AccountRepository;
import com.jag.account_service.repository.sql.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        if (!customerRepository.existsById(request.customerId())) {
            throw new IllegalArgumentException("Customer not found: " + request.customerId());
        }
        if (accountRepository.findByAccountNumber(request.accountNumber()).isPresent()) {
            throw new IllegalArgumentException("Account already exists: " + request.accountNumber());
        }

        LocalDateTime now = LocalDateTime.now();
        Account account = Account.builder()
                .accountNumber(request.accountNumber())
                .customerId(request.customerId())
                .balance(request.balance())
                .status(AccountStatus.ACTIVE)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return AccountResponse.from(accountRepository.save(account));
    }
}
