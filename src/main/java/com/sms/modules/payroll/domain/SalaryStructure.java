package com.sms.modules.payroll.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "salary_structures")
@Data
public class SalaryStructure {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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