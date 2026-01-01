package com.sms.modules.fees.mapper;

import com.sms.modules.fees.domain.Fee;
import com.sms.modules.fees.dto.FeeResponse;

public class FeeMapper {

	public static FeeResponse toResponse(Fee fee) {
		return FeeResponse.builder()
				.id(fee.getId())
				.studentId(fee.getStudentId())
				.totalAmount(fee.getTotalAmount())
				.paidAmount(fee.getPaidAmount())
				.dueAmount(fee.getDueAmount())
				.status(fee.getStatus())
				.build();
	}
}