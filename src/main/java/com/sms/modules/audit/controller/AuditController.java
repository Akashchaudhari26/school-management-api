package com.sms.modules.audit.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sms.modules.audit.domain.AuditLog;
import com.sms.modules.audit.repository.AuditLogRepository;
import com.sms.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','PRINCIPAL')")
public class AuditController {

    private final AuditLogRepository repository;

    @GetMapping
    public List<AuditLog> list() {
        return repository.findByTenantIdOrderByPerformedAtDesc(
                SecurityUtils.getTenantId());
    }
}
