package com.sms.modules.fees.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.core.exceptions.FeeNotFoundException;
import com.sms.core.exceptions.InvalidFeePaymentException;
import com.sms.core.exceptions.StudentNotFoundException;
import com.sms.modules.fees.domain.ClassFeeMaster;
import com.sms.modules.fees.domain.Fee;
import com.sms.modules.fees.domain.FeeItem;
import com.sms.modules.fees.domain.FeePayment;
import com.sms.modules.fees.domain.FeeStatus;
import com.sms.modules.fees.dto.BulkFeeCreateRequest;
import com.sms.modules.fees.dto.BulkFeeResponse;
import com.sms.modules.fees.dto.FeeCreateRequest;
import com.sms.modules.fees.dto.FeePaymentHistoryResponse;
import com.sms.modules.fees.dto.FeePaymentRequest;
import com.sms.modules.fees.dto.FeeResponse;
import com.sms.modules.fees.mapper.FeeMapper;
import com.sms.modules.fees.repository.ClassFeeMasterRepository;
import com.sms.modules.fees.repository.FeeRepository;
import com.sms.modules.fees.util.ReceiptNumberGenerator;
import com.sms.modules.student.domain.Student;
import com.sms.modules.student.repository.StudentRepository;
import com.sms.security.SecurityUtils;

@Service
public class FeeServiceImpl implements FeeService {

	@Autowired
	private FeeRepository feeRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private FeeReceiptPdfService feeReceiptPdfService;

	@Autowired
	private ClassFeeMasterRepository feeMasterRepository;

	@Override
	public FeeResponse createFee(FeeCreateRequest request) {

		Student student = studentRepository.findById(request.getStudentId())
				.orElseThrow(() -> new StudentNotFoundException(request.getStudentId()));

		boolean exists = feeRepository.existsByStudentIdAndAcademicYear(
				request.getStudentId(), request.getAcademicYear());

		if (exists) {
			throw new IllegalStateException("Fee already exists for this academic year");
		}

		BigDecimal totalAmount = request.getFeeItems().stream()
				.map(FeeItem::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		Fee fee = Fee.builder()
				.studentId(request.getStudentId())
				.academicYear(request.getAcademicYear())
				.feeItems(request.getFeeItems())
				.totalAmount(totalAmount)
				.paidAmount(BigDecimal.ZERO)
				.dueAmount(totalAmount)
				.status(FeeStatus.PENDING)
				.payments(new ArrayList<>())
				.build();

		Fee saved = feeRepository.save(fee);
		return FeeMapper.toResponse(saved, student);
	}

	// ---------------- PAYMENT ----------------

	@Override
	@Transactional
	public FeeResponse payFeeByStudent(String studentId, String academicYear, FeePaymentRequest request) {

		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new StudentNotFoundException(studentId));

		Fee fee = feeRepository.findByStudentIdAndAcademicYear(studentId, academicYear)
				.orElseThrow(() -> new FeeNotFoundException("Fee not found"));

		if (fee.getStatus() == FeeStatus.PAID) {
			throw new InvalidFeePaymentException("Fee already fully paid");
		}

		if (request.getAmount().compareTo(fee.getDueAmount()) > 0) {
			throw new InvalidFeePaymentException("Payment exceeds due amount");
		}

		FeePayment payment = FeePayment.builder()
				.receiptNo(ReceiptNumberGenerator.generate())
				.amountPaid(request.getAmount())
				.mode(request.getMode())
				.collectedBy(SecurityUtils.getCurrentUserId())
				.paidAt(LocalDateTime.now())
				.build();

		if (fee.getPayments() == null) {
			fee.setPayments(new ArrayList<>());
		}
		fee.getPayments().add(payment);

		fee.setPaidAmount(fee.getPaidAmount().add(request.getAmount()));
		fee.setDueAmount(fee.getTotalAmount().subtract(fee.getPaidAmount()));

		fee.setStatus(
				fee.getDueAmount().compareTo(BigDecimal.ZERO) == 0
						? FeeStatus.PAID
						: FeeStatus.PARTIALLY_PAID);

		Fee saved = feeRepository.save(fee);
		return FeeMapper.toResponse(saved, student);
	}

	// ---------------- READ ----------------

	@Override
	public FeeResponse getFeeByStudent(String studentId, String academicYear) {

		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new StudentNotFoundException(studentId));

		Fee fee = feeRepository.findByStudentIdAndAcademicYear(studentId, academicYear)
				.orElseThrow(() -> new FeeNotFoundException("Fee not found"));

		return FeeMapper.toResponse(fee, student);
	}

	@Override
	public List<FeeResponse> getPendingDues(String academicYear) {

		List<Fee> fees = feeRepository.findByAcademicYearAndStatusIn(
				academicYear,
				List.of(FeeStatus.PENDING, FeeStatus.PARTIALLY_PAID));

		if (fees.isEmpty()) {
			return List.of();
		}

		Map<String, Student> studentMap = studentRepository
				.findAllById(fees.stream().map(Fee::getStudentId).distinct().toList())
				.stream()
				.collect(Collectors.toMap(Student::getId, Function.identity()));

		return fees.stream()
				.map(fee -> {
					Student student = studentMap.get(fee.getStudentId());
					return student != null
							? FeeMapper.toResponse(fee, student)
							: FeeMapper.toResponse(fee);
				})
				.toList();
	}

	// ---------------- RECEIPT ----------------

	@Override
	public byte[] downloadReceipt(String receiptNo) {

		Fee fee = feeRepository.findByPaymentsReceiptNo(receiptNo)
				.orElseThrow(() -> new FeeNotFoundException("Receipt not found"));

		Student student = studentRepository.findById(fee.getStudentId())
				.orElseThrow(() -> new StudentNotFoundException(fee.getStudentId()));

		FeePayment payment = fee.getPayments().stream()
				.filter(p -> p.getReceiptNo().equals(receiptNo))
				.findFirst()
				.orElseThrow(() -> new FeeNotFoundException("Receipt not found"));

		return feeReceiptPdfService.generateReceipt(fee, payment, student);
	}

	@Override
	public List<FeePaymentHistoryResponse> getPaymentHistory(
			String studentId,
			String academicYear) {

		// 1️⃣ Validate student existence
		studentRepository.findById(studentId)
				.orElseThrow(() -> new StudentNotFoundException(studentId));

		// 2️⃣ Fetch fee
		Fee fee = feeRepository.findByStudentIdAndAcademicYear(studentId, academicYear)
				.orElseThrow(() -> new FeeNotFoundException("Fee not found"));

		// 3️⃣ Defensive: empty payments
		if (fee.getPayments() == null || fee.getPayments().isEmpty()) {
			return List.of();
		}

		// 4️⃣ Sort payments by date (latest first)
		return fee.getPayments().stream()
				.sorted(Comparator.comparing(FeePayment::getPaidAt).reversed())
				.map(p -> FeePaymentHistoryResponse.builder()
						.receiptNo(p.getReceiptNo())
						.amountPaid(p.getAmountPaid())
						.mode(p.getMode())
						.collectedBy(p.getCollectedBy())
						.paidAt(p.getPaidAt())
						.receiptDownloadUrl("/api/fees/receipt/" + p.getReceiptNo())
						.build())
				.toList();
	}

	@Override
	public BulkFeeResponse createBulkFees(BulkFeeCreateRequest request) {
		// 1. Fetch Students based on criteria
		List<Student> students;
		if (request.getSection() != null && !request.getSection().isEmpty()) {
			students = studentRepository.findByCurrentClassIdAndCurrentSection(request.getClassId(),
					request.getSection());
		} else {
			students = studentRepository.findByCurrentClassId(request.getClassId());
		}

		if (students.isEmpty()) {
			return new BulkFeeResponse(0, 0, 0, "No students found for the selected Class/Section.");
		}

		// --- OPTIMIZATION START ---

		// 2. Extract all IDs to check against the database in one go
		List<String> targetStudentIds = students.stream()
				.map(Student::getId) // Check if you use .getId() (UUID) or .getStudentId() (Admission No)
				.collect(Collectors.toList());

		// 3. Batch Fetch: Get ALL existing fee records for these students in ONE query
		List<Fee> existingFees = feeRepository.findByAcademicYearAndStudentIdIn(
				request.getAcademicYear(),
				targetStudentIds);

		// 4. Create a specific Set for O(1) instant lookup
		Set<String> studentsWithFees = existingFees.stream()
				.map(Fee::getStudentId)
				.collect(Collectors.toSet());

		List<Fee> feesToSave = new ArrayList<>();
		int skippedCount = 0;

		for (Student student : students) {

			// Check the Set instead of the Database
			if (studentsWithFees.contains(student.getId())) {
				skippedCount++;
				continue;
			}

			// Map Request to Entity
			Fee fee = new Fee();
			fee.setStudentId(student.getId());
			fee.setAcademicYear(request.getAcademicYear());

			// Map Fee Items
			List<FeeItem> items = request.getFeeItems().stream()
					.map(item -> new FeeItem(item.getName(), item.getAmount()))
					.collect(Collectors.toList());
			fee.setFeeItems(items);

			// Calculate Totals
			BigDecimal total = items.stream()
					.map(FeeItem::getAmount)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			fee.setTotalAmount(total);
			fee.setPaidAmount(BigDecimal.ZERO);
			fee.setDueAmount(total);
			fee.setStatus(FeeStatus.PENDING);

			// Add to the batch list instead of saving immediately
			feesToSave.add(fee);
		}

		// 6. Batch Save: Write ALL new records in ONE query
		if (!feesToSave.isEmpty()) {
			feeRepository.saveAll(feesToSave);
		}

		// --- OPTIMIZATION END ---

		return new BulkFeeResponse(
				students.size(),
				feesToSave.size(),
				skippedCount,
				"Bulk process completed successfully.");
	}

	// --- AUTOMATION METHOD ---
	@Override
	public void assignDefaultFeeStructure(Student student) {

		// 1. Check if a Master Structure exists for this class
		Optional<ClassFeeMaster> masterOpt = feeMasterRepository.findByClassIdAndAcademicYear(
				student.getCurrentClassId(),
				student.getCurrentAcademicYear());

		if (masterOpt.isEmpty()) {
			System.out.println(
					"No Master Fee Structure found for " + student.getCurrentClassId() + ". Skipping auto-assignment.");
			return;
		}

		ClassFeeMaster master = masterOpt.get();

		// 2. Clone the Master structure into a specific Student Fee record
		Fee fee = new Fee();
		fee.setStudentId(student.getId()); // Admission No
		fee.setAcademicYear(student.getCurrentAcademicYear());

		// Copy Items from Master
		fee.setFeeItems(master.getFeeItems());

		// Set Financials
		fee.setTotalAmount(master.getTotalAmount());
		fee.setPaidAmount(BigDecimal.ZERO);
		fee.setDueAmount(master.getTotalAmount());
		fee.setStatus(FeeStatus.PENDING);

		// 3. Save
		feeRepository.save(fee);
		System.out.println("Auto-assigned fees for student: " + student.getId());
	}

}
