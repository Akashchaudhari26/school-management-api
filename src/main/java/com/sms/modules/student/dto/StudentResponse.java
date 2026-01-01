package com.sms.modules.student.dto;


import java.time.LocalDate;
import java.util.List;

import com.sms.modules.student.domain.Gender;
import com.sms.modules.student.domain.GuardianRef;
import com.sms.modules.student.domain.StudentStatus;

import lombok.Data;

@Data
public class StudentResponse {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String admissionNumber;
    private Integer admissionYear;
    private StudentStatus status;
    private String currentClassId;
    private String currentSection;
	private String currentAcademicYear;

    private List<GuardianRef> guardians;
    // getters/setters
}
