package com.jag.transaction_service.repository.sql;

import com.jag.transaction_service.entity.sql.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionReference(String transactionReference);
    List<Transaction> findBySourceAccountOrDestinationAccountOrderByCreatedAtDesc(String source, String destination);
}