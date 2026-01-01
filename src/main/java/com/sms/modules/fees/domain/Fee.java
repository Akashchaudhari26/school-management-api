package com.sms.modules.fees.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "fees")
public class Fee {

	@Id
	private String id;

	private String studentId;
	private String academicYear;

	private List<FeeItem> feeItems;
	private BigDecimal totalAmount;
	private BigDecimal paidAmount;
	private BigDecimal dueAmount;

	private FeeStatus status;
	private List<FeePayment> payments;
}