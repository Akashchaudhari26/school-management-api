package com.sms.modules.audit.domain;

public enum AuditAction {
    CREATE,
    UPDATE,
    DELETE,

    // Business actions
    MARK_ATTENDANCE,
    MARK_BULK_ATTENDANCE,
    UPDATE_ATTENDANCE,
    DELETE_ATTENDANCE,
    APPROVE_LEAVE,
    REJECT_LEAVE,
    PROMOTE_STUDENT,
    GENERATE_PAYROLL,
    PAY_SALARY,
    COLLECT_FEE,
    UPDATE_MARKS,
    LOGIN,
    LOGOUT
}
