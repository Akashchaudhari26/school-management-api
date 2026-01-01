package com.sms.modules.fees.dto;

import com.sms.modules.fees.domain.FeeStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FeeDueResponse {

    private String feeId;
    private String studentId;
    private String academicYear;

    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal dueAmount;

    private FeeStatus status;
}
