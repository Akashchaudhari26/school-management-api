package com.sms.modules.student.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import com.sms.modules.iam.domain.User;
import com.sms.modules.student.domain.Student;
import com.sms.modules.student.domain.StudentStatus;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentRepository extends MongoRepository<Student, String> {
	Page<Student> findByAdmissionYear(Integer year, Pageable pageable);

	Page<Student> findByCurrentClassId(String classId, Pageable pageable);

	boolean existsByAdmissionNumber(String admissionNumber);

	List<Student> findByGuardiansAdharNumber(String adharNumber);

	@Query("""
			{
			  guardians: {
			    $elemMatch: {
			      adharNumber: ?0,
			      $or: [
			        { iamUserId: null },
			        { iamUserId: "" }
			      ]
			    }
			  }
			}
			""")
	@Update("""
			{
			  $set: { "guardians.$.iamUserId": ?1 }
			}
			""")
	void linkGuardianToIamUser(String adharNumber, String iamUserId);

	List<Student> findByGuardiansIamUserId(String iamUserId);

	boolean existsByIdAndGuardiansIamUserId(String studentId, String parentUserId);

	// Remove "True" from the name and add StudentStatus as a parameter
	List<Student> findByCurrentClassIdAndCurrentSectionAndStatus(String classId, String section, StudentStatus status);

	List<Student> findByCurrentClassId(String classId);

	List<Student> findByCurrentClassIdAndCurrentSection(String classId, String section);

}
