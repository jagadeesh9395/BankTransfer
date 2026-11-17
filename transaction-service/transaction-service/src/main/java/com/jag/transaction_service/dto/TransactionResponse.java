package com.jag.transaction_service.dto;

import com.jag.transaction_service.entity.sql.Transaction;
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
) {
    public static TransactionResponse fromEntity(Transaction entity) {
        return TransactionResponse.builder()
                .transactionReference(entity.getTransactionReference())
                .sourceAccount(entity.getSourceAccount())
                .destinationAccount(entity.getDestinationAccount())
                .amount(entity.getAmount())
                .status(entity.getStatus().name())
                .timestamp(entity.getCreatedAt())
                .build();
    }
}