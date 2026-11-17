package com.jag.account_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record TransferRequest(
    @NotBlank(message = "From account number is mandatory")
    String fromAccountNumber,

    @NotBlank(message = "To account number is mandatory")
    String toAccountNumber,

    @NotNull(message = "Amount is mandatory")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    BigDecimal amount,

    @Size(max = 255, message = "Performed by must not exceed 255 characters")
    String performedBy
) {}
