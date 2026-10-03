package com.sms.modules.audit.domain;

import lombok.*;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

import java.time.Instant;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "audit_logs", indexes = @Index(name = "idx_audit_expiry", columnList = "expiry_at"))
public class AuditLog {

    @Id

    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String tenantId;
    private String entityName; // STUDENT, FEE, ATTENDANCE
    private String entityId;
    @Enumerated(EnumType.STRING)
    private AuditAction action;

    private String performedByUserId;
    private String performedByRole;

    private Instant performedAt;

    // Full diff
    @JdbcTypeCode(SqlTypes.JSON)
    private Object oldValue;
    @JdbcTypeCode(SqlTypes.JSON)
    private Object newValue;

    private String message;

    // Auto-expire after 1 year
    private Instant expiryAt;
}
