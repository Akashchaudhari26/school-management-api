package com.sms.modules.fees.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

import com.sms.modules.fees.domain.FeeItem;
import com.sms.modules.fees.domain.FeePayment;
import com.sms.modules.fees.domain.FeeStatus;

@Data
@Builder
public class FeeResponse {
    private String id;

	private String studentId;
	private BigDecimal totalAmount;
	private BigDecimal paidAmount;
	private BigDecimal dueAmount;

	private FeeStatus status;
}