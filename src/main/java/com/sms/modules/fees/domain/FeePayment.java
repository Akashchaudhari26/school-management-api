package com.sms.modules.fees.domain;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.index.Indexed;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeePayment {
    @Indexed(unique = true)
    private String	  receiptNo;
    private BigDecimal	  amountPaid;
    private String	  mode;	      // CASH, UPI, CARD
    private String	  collectedBy;
    private LocalDateTime paidAt;
}