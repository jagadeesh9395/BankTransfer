package com.jag.transaction_service.entity.nosql;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document(collection = "transaction_event_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEventLog {

    @Id
    private String id;
    private String transactionReference;
    private String idempotencyKey;
    private String sourceAccount;
    private String destinationAccount;
    private String rawPayload;
    private Instant loggedAt;
    private Map<String, Object> metadata;
}
