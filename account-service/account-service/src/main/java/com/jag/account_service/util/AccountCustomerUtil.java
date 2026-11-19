package com.jag.account_service.util;

import com.jag.account_service.entity.sql.Account;
import com.jag.account_service.entity.sql.Customer;
import com.jag.account_service.repository.sql.AccountRepository;
import com.jag.account_service.repository.sql.CustomerRepository;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountCustomerUtil {

    public AccountRepository accountRepository;

    public CustomerRepository customerRepository;

    public static String getPerformedBy(String fromAccountNumber) {
        return accountRepository.findByAccountNumber(fromAccountNumber)
                .map(Account::getCustomerId)
                .orElse("UNKNOWN");
    }

    public static String getCustomerName(String customerId) {
        return customerRepository.findById(customerId)
                .map(Customer::getName)
                .orElse("UNKNOWN");
    }

}
