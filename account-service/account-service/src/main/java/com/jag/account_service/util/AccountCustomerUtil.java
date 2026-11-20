package com.jag.account_service.util;

import com.jag.account_service.entity.sql.Account;
import com.jag.account_service.entity.sql.Customer;
import com.jag.account_service.entity.sql.UserLogin;
import com.jag.account_service.repository.sql.AccountRepository;
import com.jag.account_service.repository.sql.CustomerRepository;
import com.jag.account_service.repository.sql.UserLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountCustomerUtil {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final UserLoginRepository userLoginRepository;

    public String getPerformedBy(String fromAccountNumber) {
        return accountRepository.findByAccountNumber(fromAccountNumber)
                .map(Account::getCustomerId)
                .orElse("UNKNOWN");
    }

    public String getCustomerName(String customerId) {
        return customerRepository.findById(customerId)
                .map(Customer::getName)
                .orElse("UNKNOWN");
    }

    public String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    public String getAuthenticatedCustomerId() {
        String username = getAuthenticatedUsername();
        if (username == null) {
            return null;
        }
        return userLoginRepository.findByUsername(username)
                .map(UserLogin::getCustomerId)
                .orElse(null);
    }

    public String getAuthenticatedAccountNumber() {
        String username = getAuthenticatedUsername();
        if (username == null) {
            return null;
        }
        return userLoginRepository.findByUsername(username)
                .map(UserLogin::getAccountNumber)
                .orElse(null);
    }

    public String getAuthenticatedCustomerName() {
        String customerId = getAuthenticatedCustomerId();
        if (customerId == null) {
            return null;
        }
        return getCustomerName(customerId);
    }

    public String getCustomerNameByUsername(String username) {
        if (username == null) {
            return null;
        }
        return userLoginRepository.findByUsername(username)
                .map(UserLogin::getCustomerId)
                .map(this::getCustomerName)
                .orElse(null);
    }

    public String getAccountNumberByUsername(String username) {
        if (username == null) {
            return null;
        }
        return userLoginRepository.findByUsername(username)
                .map(UserLogin::getAccountNumber)
                .orElse(null);
    }
}
