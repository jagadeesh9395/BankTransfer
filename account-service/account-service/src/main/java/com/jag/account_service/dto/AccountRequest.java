package com.jag.account_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record AccountRequest(
    @NotBlank(message = "Account number is mandatory")
    @Size(max = 34, message = "Account number must not exceed 34 characters")
    String accountNumber,

    @NotBlank(message = "Customer ID is mandatory")
    String customerId,

    @NotNull(message = "Balance is mandatory")
    @DecimalMin(value = "0.00", message = "Balance cannot be negative")
    @Digits(integer = 15, fraction = 4, message = "Balance must have at most 15 integer digits and 4 decimal places")
    BigDecimal balance
) {}
