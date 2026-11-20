package com.jag.account_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRegisterRequest(
    @NotBlank(message = "Username is mandatory")
    @Size(max = 255, message = "Username must not exceed 255 characters")
    String username,

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    String password,

    @NotBlank(message = "Customer ID is mandatory")
    String customerId,

    @NotBlank(message = "Account number is mandatory")
    String accountNumber
) {}
