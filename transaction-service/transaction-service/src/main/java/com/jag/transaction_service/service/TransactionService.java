package com.jag.transaction_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jag.transaction_service.dto.TransactionRequest;
import com.jag.transaction_service.dto.TransactionResponse;
import com.jag.transaction_service.entity.nosql.TransactionEventLog;
import com.jag.transaction_service.entity.sql.IdempotencyRecord;
import com.jag.transaction_service.entity.sql.Transaction;
import com.jag.transaction_service.entity.sql.TransactionStatus;
import com.jag.transaction_service.repository.nosql.TransactionEventLogRepository;
import com.jag.transaction_service.repository.sql.IdempotencyRecordRepository;
import com.jag.transaction_service.repository.sql.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final IdempotencyRecordRepository idempotencyRecordRepository;
    private final TransactionEventLogRepository eventLogRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public TransactionResponse processTransaction(TransactionRequest request) {
        // 1. Idempotency verification: Check if eventId was already processed
        Optional<IdempotencyRecord> existingKey = idempotencyRecordRepository.findById(request.transactionReference());
        if (existingKey.isPresent()) {
            log.info("Duplicate transaction request caught: {}", request.transactionReference());
            return transactionRepository.findByTransactionReference(existingKey.get().getTransactionReference())
                    .map(TransactionResponse::fromEntity)
                    .orElseThrow(() -> new IllegalStateException("Corrupt ledger state for reference: " + existingKey.get().getTransactionReference()));
        }

        // 2. Generate unique ledger reference
        String internalRef = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 3. Persist transaction into MySQL ledger
        Transaction transaction = Transaction.builder()
                .transactionReference(internalRef)
                .sourceAccount(request.sourceAccount())
                .destinationAccount(request.destinationAccount())
                .amount(request.amount())
                .transactionType(request.transactionType())
                .status(TransactionStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);

        // 4. Save idempotency key mapped to internal reference
        IdempotencyRecord idempotencyRecord = IdempotencyRecord.builder()
                .key(request.transactionReference())
                .transactionReference(internalRef)
                .createdAt(LocalDateTime.now())
                .build();
        idempotencyRecordRepository.save(idempotencyRecord);

        // 5. Fire async MongoDB document audit log
        logTransactionEventAsync(request, internalRef);

        return TransactionResponse.fromEntity(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactions(String accountNumber) {
        return transactionRepository.findBySourceAccountOrDestinationAccountOrderByCreatedAtDesc(accountNumber, accountNumber)
                .stream()
                .map(TransactionResponse::fromEntity)
                .toList();
    }

    @Async
    public void logTransactionEventAsync(TransactionRequest request, String internalRef) {
        try {
            TransactionEventLog logEntry = TransactionEventLog.builder()
                    .transactionReference(internalRef)
                    .idempotencyKey(request.transactionReference())
                    .sourceAccount(request.sourceAccount())
                    .destinationAccount(request.destinationAccount())
                    .rawPayload(objectMapper.writeValueAsString(request))
                    .loggedAt(Instant.now())
                    .metadata(Map.of(
                            "origin", request.origin() != null ? request.origin() : "AccountService-FeignClient",
                            "engineVersion", request.engineVersion() != null ? request.engineVersion() : "1.0.0"
                    ))
                    .build();
            eventLogRepository.save(logEntry);
        } catch (Exception ex) {
            log.error("Failed to write async MongoDB transaction log for ref: {}", internalRef, ex);
        }
    }
}
