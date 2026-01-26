package com.sms.modules.payroll.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDate;

@Data
@Document(collection = "payroll_transactions")
public class PayrollTransaction {
    @Id
    private String id;
    private String staffId;
    private String staffName; // Cached for easier reporting

    private String month; // e.g., "JANUARY"
    private Integer year; // e.g., 2026

    // Attendance Snapshot
    private Integer totalDaysInMonth;
    private Integer presentDays;
    private Integer paidLeaves;
    private Integer absentDays;
    private Integer payableDays; // (Present + Paid Leaves)

    // Financial Snapshot (Actual Calculated Values)
    private Double basicEarned;
    private Double hraEarned;
    private Double totalEarnings;
    private Double totalDeductions;
    private Double netPayable;

    private String status; // DRAFT, GENERATED, PAID
    private LocalDate paymentDate;
    private LocalDate generatedDate;
}