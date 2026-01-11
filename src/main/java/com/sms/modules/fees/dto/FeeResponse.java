package com.sms.modules.fees.dto;

import java.math.BigDecimal;
import java.util.List;

import com.sms.modules.fees.domain.FeeItem;
import com.sms.modules.fees.domain.FeePayment;
import com.sms.modules.fees.domain.FeeStatus;
import com.sms.modules.student.domain.GuardianRef;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeeResponse {
	private String id;

	private String studentId;
	private String studentName;
	private List<GuardianRef> guardian;
	private String academicYear;
	private String currentClassId;
	private String currentSection;
	private List<FeeItem> feeItems;
	private List<FeePayment> payments;

	private BigDecimal totalAmount;
	private BigDecimal paidAmount;
	private BigDecimal dueAmount;

	private FeeStatus status;
}