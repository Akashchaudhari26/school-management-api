package com.sms.modules.fees.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeePaymentHistoryResponse {
    private String receiptNo;
    private BigDecimal amountPaid;
    private String mode;
    private String collectedBy;
    private LocalDateTime paidAt;
    private String receiptDownloadUrl;
}
