package com.sms.modules.payroll.dto;

import lombok.Data;

@Data
public class SalaryStructureDTO {
    private String staffId;
    private String staffName; // Cached for easier reporting

    private Double basicSalary;
    private Double hra;
    private Double da;
    private Double transportAllowance;
    private Double providentFund;
    private Double professionalTax;
}