package com.jag.account_service.repository.nosql;

import com.jag.account_service.entity.nosql.AccountAuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountAuditRepository extends MongoRepository<AccountAuditLog, String> {
}
