package com.sms.modules.audit.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sms.modules.audit.domain.AuditLog;

import java.util.List;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {

    List<AuditLog> findByTenantIdOrderByPerformedAtDesc(String tenantId);

    List<AuditLog> findByTenantIdAndEntityName(
            String tenantId,
            String entityName);
}
