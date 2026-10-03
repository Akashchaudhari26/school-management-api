package com.sms.modules.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sms.modules.audit.domain.AuditLog;

import java.util.List;
import java.time.Instant;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, String> {

    List<AuditLog> findByTenantIdOrderByPerformedAtDesc(String tenantId);

    List<AuditLog> findByTenantIdAndEntityName(
            String tenantId,
            String entityName);

    @Transactional
    long deleteByExpiryAtBefore(Instant cutoff);
}
