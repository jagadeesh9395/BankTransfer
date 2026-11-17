package com.jag.transaction_service.repository.sql;

import com.jag.transaction_service.entity.sql.IdempotencyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotencyRecordRepository extends JpaRepository<IdempotencyRecord, String> {
}