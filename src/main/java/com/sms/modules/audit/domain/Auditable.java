package com.sms.modules.audit.domain;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {

    AuditAction action();

    String entity(); // STUDENT, ATTENDANCE, FEE

    boolean captureOldValue() default true;
}
