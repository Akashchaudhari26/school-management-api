package com.sms.modules.audit.service;

import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.modules.audit.repository.AuditLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditRetentionService {

    private final AuditLogRepository auditLogRepository;

    @Scheduled(cron = "${audit.retention-cron:0 30 2 * * *}")
    @Transactional
    public void deleteExpiredAuditLogs() {
        auditLogRepository.deleteByExpiryAtBefore(Instant.now());
    }
}
