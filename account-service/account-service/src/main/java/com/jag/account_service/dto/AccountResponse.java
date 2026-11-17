package com.jag.account_service.dto;

import com.jag.account_service.entity.sql.Account;
import com.jag.account_service.entity.sql.AccountStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(
    Long id,
    String accountNumber,
    String customerId,
    BigDecimal balance,
    AccountStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getCustomerId(),
                account.getBalance(),
                account.getStatus(),
                account.getCreatedAt(),
                account.getUpdatedAt()
        );
    }
}
