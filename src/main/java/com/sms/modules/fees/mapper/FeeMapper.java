package com.sms.modules.fees.mapper;

import com.sms.modules.fees.domain.Fee;
import com.sms.modules.fees.dto.FeeResponse;
import com.sms.modules.student.domain.Student;

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

	public static FeeResponse toResponse(Fee fee, Student student) {
		return FeeResponse.builder()
				.id(fee.getId())
				.studentId(fee.getStudentId())
				.studentName(student.getFirstName() + " " + student.getLastName())
				.guardian(student.getGuardians()) // Ensure types match (List<GuardianRef>)
				.academicYear(fee.getAcademicYear()) // Use Fee's academic year usually
				.currentClassId(student.getCurrentClassId())
				.currentSection(student.getCurrentSection())
				.feeItems(fee.getFeeItems())
				.payments(fee.getPayments())
				.totalAmount(fee.getTotalAmount())
				.paidAmount(fee.getPaidAmount())
				.dueAmount(fee.getDueAmount())
				.status(fee.getStatus())
				.build();
	}
}