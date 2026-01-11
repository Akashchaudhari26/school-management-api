package com.sms.modules.fees.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sms.modules.fees.domain.Fee;
import com.sms.modules.fees.domain.FeeStatus;

@Repository
public interface FeeRepository extends MongoRepository<Fee, String> {
	Optional<Fee> findByStudentIdAndAcademicYear(String studentId, String academicYear);

	List<Fee> findByAcademicYearAndStatusIn(String academicYear, List<FeeStatus> statuses);

	Optional<Fee> findByPaymentsReceiptNo(String receiptNo);

	boolean existsByStudentIdAndAcademicYear(String studentId, String academicYear);

	List<Fee> findByAcademicYearAndStudentIdIn(String academicYear, List<String> studentIds);
}
