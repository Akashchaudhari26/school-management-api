package com.sms.modules.student.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Document(collection = "students")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
	@Id
	private String id; // ObjectId string

	// core identity
	@Indexed
	private String firstName;
	private String middleName;
	@Indexed
	private String lastName;
	private LocalDate dateOfBirth;
	private Gender gender; // ENUM recommended
	@Indexed(unique = true)
	private String adharNumber;
	private String photoUrl; // GridFS id or URL

	// admission lifecycle
	@Indexed(unique = true)
	private String admissionNumber; // unique
	private Integer admissionYear;
	private StudentStatus status; // ACTIVE, INACTIVE, ALUMNI, DROPPED, ON_LEAVE

	@Indexed
	private String currentClassId; // class mapping id or string like "10"

	@Indexed
	private String currentSection; // A, B, etc.

	// guardian references (can link to IAM users)
	private List<GuardianRef> guardians;

	// basic contact
	private String phone;
	@Indexed
	private String email;
	private Address permanentAddress;
	private Address presentAddress;

	// health
	private HealthRecord healthRecord;

	// metadata
	private String createdBy; // IAM user id
	private String updatedBy;
	private long createdAt;
	private long updatedAt;

	@Indexed
	private String currentAcademicYear;

	// misc attributes for flexible search
	private Map<String, Object> attributes;
	// getters/setters, constructors, equals, hashCode
}
