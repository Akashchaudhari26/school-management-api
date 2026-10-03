package com.sms.modules.fees.domain;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class FeePayment {
    @Column(unique = true)
    private String receiptNo;
    @Column(precision = 12, scale = 2)
    private BigDecimal amountPaid;
    private String mode; // CASH, UPI, CARD
    private String collectedBy;
    private LocalDateTime paidAt;
}
