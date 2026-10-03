package com.sms.modules.student.domain;

import jakarta.persistence.Id;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "students")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id; // UUID string exposed to the API

	// core identity
	private String firstName;
	private String middleName;
	private String lastName;
	private LocalDate dateOfBirth;
	@Enumerated(EnumType.STRING)
	private Gender gender; // ENUM recommended
	@Column(unique = true)
	private String adharNumber;
	private String photoUrl; // Stored file key or external URL

	// admission lifecycle
	@Column(unique = true)
	private String admissionNumber; // unique
	private Integer admissionYear;
	@Enumerated(EnumType.STRING)
	private StudentStatus status; // ACTIVE, INACTIVE, ALUMNI, DROPPED, ON_LEAVE
	private String currentClassId; // class mapping id or string like "10"
	private String currentSection; // A, B, etc.

	// guardian references (can link to IAM users)
	@ElementCollection
	@CollectionTable(name = "student_guardians", joinColumns = @jakarta.persistence.JoinColumn(name = "student_id"))
	@OrderColumn(name = "guardian_order")
	@AttributeOverrides({
			@AttributeOverride(name = "adharNumber", column = @jakarta.persistence.Column(name = "adhar_number")),
			@AttributeOverride(name = "name", column = @jakarta.persistence.Column(name = "name")),
			@AttributeOverride(name = "relation", column = @jakarta.persistence.Column(name = "relation")),
			@AttributeOverride(name = "phone", column = @jakarta.persistence.Column(name = "phone")),
			@AttributeOverride(name = "email", column = @jakarta.persistence.Column(name = "email")),
			@AttributeOverride(name = "iamUserId", column = @jakarta.persistence.Column(name = "iam_user_id")),
			@AttributeOverride(name = "primary", column = @jakarta.persistence.Column(name = "is_primary"))
	})
	private List<GuardianRef> guardians;

	// basic contact
	private String phone;
	private String email;
	@JdbcTypeCode(SqlTypes.JSON)
	private Address permanentAddress;
	@JdbcTypeCode(SqlTypes.JSON)
	private Address presentAddress;

	// health
	@JdbcTypeCode(SqlTypes.JSON)
	private HealthRecord healthRecord;

	// metadata
	private String createdBy; // IAM user id
	private String updatedBy;
	private long createdAt;
	private long updatedAt;
	private String currentAcademicYear;

	// misc attributes for flexible search
	@JdbcTypeCode(SqlTypes.JSON)
	private Map<String, Object> attributes;
	// getters/setters, constructors, equals, hashCode
}
