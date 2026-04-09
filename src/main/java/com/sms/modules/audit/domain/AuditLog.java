package com.sms.modules.audit.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "audit_logs")
public class AuditLog {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String entityName; // STUDENT, FEE, ATTENDANCE
    private String entityId;

    @Indexed
    private AuditAction action;

    private String performedByUserId;
    private String performedByRole;

    private Instant performedAt;

    // Full diff
    private Object oldValue;
    private Object newValue;

    private String message;

    // Auto-expire after 1 year
    @Indexed(expireAfterSeconds = 31536000) // 365 days
    private Instant expiryAt;
}
