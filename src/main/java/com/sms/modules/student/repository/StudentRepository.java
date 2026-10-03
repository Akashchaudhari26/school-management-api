package com.sms.modules.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sms.modules.iam.domain.User;
import com.sms.modules.student.domain.Student;
import com.sms.modules.student.domain.StudentStatus;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentRepository extends JpaRepository<Student, String>, JpaSpecificationExecutor<Student> {
	Page<Student> findByAdmissionYear(Integer year, Pageable pageable);

	Page<Student> findByCurrentClassId(String classId, Pageable pageable);

	boolean existsByAdmissionNumber(String admissionNumber);

	List<Student> findByGuardiansAdharNumber(String adharNumber);
	List<Student> findByGuardiansIamUserId(String iamUserId);
	boolean existsByIdAndGuardiansIamUserId(String studentId, String parentUserId);

	// Remove "True" from the name and add StudentStatus as a parameter
	List<Student> findByCurrentClassIdAndCurrentSectionAndStatus(String classId, String section, StudentStatus status);

	List<Student> findByCurrentClassId(String classId);

	List<Student> findByCurrentClassIdAndCurrentSection(String classId, String section);

}
