package com.jag.transaction_service.entity.sql;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_keys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdempotencyRecord {
    @Id
    @Column(name = "idempotency_key", nullable = false, length = 64)
    private String key; // eventId from Account Service

    @Column(nullable = false, length = 64)
    private String transactionReference;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
