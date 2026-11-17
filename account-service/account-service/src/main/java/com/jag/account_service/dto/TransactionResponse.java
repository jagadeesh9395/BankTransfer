package com.jag.account_service.dto;

import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionResponse(
    String transactionReference,
    String sourceAccount,
    String destinationAccount,
    BigDecimal amount,
    String status,
    LocalDateTime timestamp
) {}