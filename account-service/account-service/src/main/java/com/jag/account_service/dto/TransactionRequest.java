package com.jag.account_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransactionRequest(
    @NotBlank(message = "transactionReference / eventId is mandatory")
    String transactionReference,

    @NotBlank(message = "Source account cannot be blank")
    String sourceAccount,

    @NotBlank(message = "Destination account cannot be blank")
    String destinationAccount,

    @NotNull(message = "Amount is mandatory")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    BigDecimal amount,

    @NotBlank(message = "Transaction type is mandatory")
    String transactionType
) {}