package com.sms.modules.payroll.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "salary_structures")
@Data
public class SalaryStructure {
    @Id
    private String id;
    private String staffId; // Foreign Key to your Staff Collection
    private String staffName; // Cached for easier reporting

    // Earnings
    private Double basicSalary;
    private Double hra;
    private Double da;
    private Double transportAllowance;
    private Double specialAllowance;

    // Deductions (Fixed)
    private Double providentFund;
    private Double professionalTax;

    // Calculated
    private Double grossSalary; // Total Earnings without deductions
    private Double netSalary; // In hand (Gross - Deductions)
}