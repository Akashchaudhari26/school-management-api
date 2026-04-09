package com.sms.modules.audit.domain;

import java.time.Instant;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.modules.audit.repository.AuditLogRepository;
import com.sms.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    @Value("${audit.enabled:false}")
    private boolean auditEnabled;

    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint pjp, Auditable auditable) throws Throwable {

        if (!auditEnabled) {
            return pjp.proceed();
        }

        Object oldValue = null;
        if (auditable.captureOldValue()) {
            oldValue = null; // optional later
        }

        Object result = pjp.proceed();

        AuditLog log = AuditLog.builder()
                .tenantId(SecurityUtils.getTenantId())
                .entityName(auditable.entity())
                .entityId(extractEntityId(result))
                .action(auditable.action())
                .performedByUserId(SecurityUtils.getCurrentUserId())
                .performedByRole(SecurityUtils.getCurrentRole())
                .performedAt(Instant.now())
                .oldValue(convert(oldValue))
                .newValue(convert(result))
                .message(buildMessage(auditable))
                .expiryAt(Instant.now().plusSeconds(31536000)) // 1 year
                .build();

        auditLogRepository.save(log);
        return result;
    }

    private Object convert(Object obj) {
        if (obj == null)
            return null;
        return objectMapper.valueToTree(obj); // JsonNode (safe for arrays, objects, nulls)
    }

    private String extractEntityId(Object result) {
        try {
            return String.valueOf(
                    result.getClass().getMethod("getId").invoke(result));
        } catch (Exception e) {
            return null;
        }
    }

    private String buildMessage(Auditable auditable) {
        return auditable.action() + " performed on " + auditable.entity();
    }
}
