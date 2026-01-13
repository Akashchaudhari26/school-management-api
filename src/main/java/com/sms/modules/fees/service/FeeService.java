package com.sms.modules.fees.service;

import java.util.List;

import com.sms.modules.fees.dto.BulkFeeCreateRequest;
import com.sms.modules.fees.dto.BulkFeeResponse;
import com.sms.modules.fees.dto.FeeCreateRequest;
import com.sms.modules.fees.dto.FeeDueResponse;
import com.sms.modules.fees.dto.FeePaymentHistoryResponse;
import com.sms.modules.fees.dto.FeePaymentRequest;
import com.sms.modules.fees.dto.FeeResponse;
import com.sms.modules.student.domain.Student;

public interface FeeService {
	FeeResponse createFee(FeeCreateRequest request);

	FeeResponse payFeeByStudent(String studentId, String academicYear, FeePaymentRequest request);

	FeeResponse getFeeByStudent(String studentId, String academicYear);

	List<FeeResponse> getPendingDues(String academicYear);

	byte[] downloadReceipt(String receiptNo);

	List<FeePaymentHistoryResponse> getPaymentHistory(String studentId, String academicYear);

	BulkFeeResponse createBulkFees(BulkFeeCreateRequest request);

	void assignDefaultFeeStructure(Student student);

	FeeResponse updatePayment(String receiptNo, FeePaymentRequest updateRequest);
}
