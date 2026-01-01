package com.sms.modules.fees.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sms.modules.fees.domain.Fee;
import com.sms.modules.fees.domain.FeeItem;
import com.sms.modules.fees.domain.FeePayment;
import com.sms.modules.fees.domain.FeeStatus;
import com.sms.modules.fees.dto.FeeCreateRequest;
import com.sms.modules.fees.dto.FeeDueResponse;
import com.sms.modules.fees.dto.FeePaymentHistoryResponse;
import com.sms.modules.fees.dto.FeePaymentRequest;
import com.sms.modules.fees.dto.FeeResponse;
import com.sms.modules.fees.mapper.FeeMapper;
import com.sms.modules.fees.repository.FeeRepository;
import com.sms.modules.fees.util.ReceiptNumberGenerator;
import com.sms.modules.student.domain.Student;
import com.sms.modules.student.repository.StudentRepository;
import com.sms.security.SecurityUtils;

@Service
public class FeeServiceImpl implements FeeService {

    @Autowired
    private FeeRepository repository;

    @Autowired
    private FeeReceiptPdfService feeReceiptPdfService;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public FeeResponse createFee(FeeCreateRequest request) {

	BigDecimal total = request.getFeeItems().stream().map(FeeItem::getAmount).reduce(BigDecimal.ZERO,
		BigDecimal::add);

	Fee fee = Fee.builder().studentId(request.getStudentId()).academicYear(request.getAcademicYear())
		.feeItems(request.getFeeItems()).totalAmount(total).paidAmount(BigDecimal.ZERO).dueAmount(total)
		.status(FeeStatus.PENDING).payments(new ArrayList<>()).build();

	return FeeMapper.toResponse(repository.save(fee));
    }

    @Override
    public FeeResponse payFeeByStudent(String studentId, String academicYear, FeePaymentRequest request) {

	String receiptNo = ReceiptNumberGenerator.generate();
	Fee    fee	 = repository.findByStudentIdAndAcademicYear(studentId, academicYear)
		.orElseThrow(() -> new RuntimeException("Fee record not found"));

	FeePayment payment = FeePayment.builder().receiptNo(receiptNo).amountPaid(request.getAmount())
		.mode(request.getMode()).collectedBy(SecurityUtils.getCurrentUserId()).paidAt(LocalDateTime.now())
		.build();

	if (fee.getStatus() == FeeStatus.PAID) {
	    throw new IllegalStateException("Fee already fully paid");
	}
	
	if (request.getAmount().compareTo(fee.getDueAmount()) > 0) {
	    throw new IllegalArgumentException("Payment amount exceeds due amount");
	}

	fee.getPayments().add(payment);
	fee.setPaidAmount(fee.getPaidAmount().add(request.getAmount()));
	fee.setDueAmount(fee.getTotalAmount().subtract(fee.getPaidAmount()));

	if (fee.getDueAmount().compareTo(BigDecimal.ZERO) == 0) {
	    fee.setStatus(FeeStatus.PAID);
	} else {
	    fee.setStatus(FeeStatus.PARTIALLY_PAID);
	}

	return FeeMapper.toResponse(repository.save(fee));
    }

    @Override
    public FeeResponse getFeeByStudent(String studentId, String academicYear) {
	Fee fee = repository.findByStudentIdAndAcademicYear(studentId, academicYear)
		.orElseThrow(() -> new RuntimeException("Fee not found"));
	return FeeMapper.toResponse(fee);
    }

    @Override
    public List<FeeDueResponse> getPendingDues(String academicYear) {
	// TODO Auto-generated method stub
	List<FeeStatus> pendingStatuses = List.of(FeeStatus.PENDING, FeeStatus.PARTIALLY_PAID);

	return repository.findByAcademicYearAndStatusIn(academicYear, pendingStatuses).stream()
		.map(fee -> FeeDueResponse.builder().feeId(fee.getId()).studentId(fee.getStudentId())
			.academicYear(fee.getAcademicYear()).totalAmount(fee.getTotalAmount())
			.paidAmount(fee.getPaidAmount()).dueAmount(fee.getDueAmount()).status(fee.getStatus()).build())
		.toList();
    }

    @Override
    public byte[] downloadReceipt(String receiptNo) {
	// TODO Auto-generated method stub

	Fee fee = repository.findByPaymentsReceiptNo(receiptNo)
		.orElseThrow(() -> new RuntimeException("Receipt not found"));

	Student student = studentRepository.findById(fee.getStudentId())
		.orElseThrow(() -> new RuntimeException("Student not found with ID: " + fee.getStudentId()));

	FeePayment payment = fee.getPayments().stream().filter(p -> p.getReceiptNo().equals(receiptNo)).findFirst()
		.orElseThrow(() -> new RuntimeException("Receipt not found"));

	return feeReceiptPdfService.generateReceipt(fee, payment, student);

    }

    @Override
    public List<FeePaymentHistoryResponse> getPaymentHistory(String studentId, String academicYear) {
	Fee fee = repository.findByStudentIdAndAcademicYear(studentId, academicYear)
		.orElseThrow(() -> new RuntimeException("Fee not found"));

	return fee.getPayments().stream()
		.map(p -> FeePaymentHistoryResponse.builder().receiptNo(p.getReceiptNo()).amountPaid(p.getAmountPaid())
			.mode(p.getMode()).collectedBy(p.getCollectedBy()).paidAt(p.getPaidAt())
			.receiptDownloadUrl("/api/fees/receipt/" + p.getReceiptNo()).build())
		.toList();
    }

}
