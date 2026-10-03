package com.sms.modules.payroll.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "payroll_transactions")
public class PayrollTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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