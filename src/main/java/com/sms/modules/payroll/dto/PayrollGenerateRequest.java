package com.sms.modules.payroll.dto;

import lombok.Data;

@Data
public class PayrollGenerateRequest {
    private String month; // Enum String e.g. "JANUARY"
    private Integer year;
}