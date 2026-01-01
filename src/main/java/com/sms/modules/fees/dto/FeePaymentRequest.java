package com.sms.modules.fees.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class FeePaymentRequest {
    private BigDecimal amount;
    private String     mode;
    private String     collectedBy;
}