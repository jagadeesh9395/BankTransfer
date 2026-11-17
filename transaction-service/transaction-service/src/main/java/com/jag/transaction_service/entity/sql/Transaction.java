package com.jag.transaction_service.entity.sql;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String transactionReference; // Internal unique reference (e.g. TXN-UUID)

    @Column(nullable = false, length = 34)
    private String sourceAccount;

    @Column(nullable = false, length = 34)
    private String destinationAccount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 30)
    private String transactionType; // DEBIT_TRANSFER, CREDIT_TRANSFER

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionStatus status; // SUCCESS, FAILED, REVERSED

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
