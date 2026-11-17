package com.jag.account_service.entity.nosql;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.Map;

@Document(collection = "account_audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountAuditLog {

    @Id
    private String id;
    private String accountNumber;
    private String action; // e.g., "BALANCE_INQUIRY", "TRANSFER_INITIATED"
    private String performedBy;
    private Instant timestamp;
    private Map<String, Object> metadata;
}