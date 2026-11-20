package com.jag.account_service.dto;

import com.jag.account_service.entity.sql.UserLogin;
import java.time.LocalDateTime;

public record UserLoginResponse(
    Long id,
    String username,
    String customerId,
    String accountNumber,
    Boolean active,
    LocalDateTime createdAt,
    LocalDateTime lastLoginAt
) {
    public static UserLoginResponse from(UserLogin userLogin) {
        return new UserLoginResponse(
                userLogin.getId(),
                userLogin.getUsername(),
                userLogin.getCustomerId(),
                userLogin.getAccountNumber(),
                userLogin.getActive(),
                userLogin.getCreatedAt(),
                userLogin.getLastLoginAt()
        );
    }
}
