package com.jag.transaction_service.repository.nosql;

import com.jag.transaction_service.entity.nosql.TransactionEventLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionEventLogRepository extends MongoRepository<TransactionEventLog, String> {
}