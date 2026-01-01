package com.sms.modules.student.dto;

import java.time.LocalDate;
import java.util.List;

import com.sms.modules.student.domain.Gender;
import com.sms.modules.student.domain.GuardianRef;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentCreateRequest {
    @NotBlank
    private String firstName;
    private String middleName;
    @NotBlank
    private String lastName;

    @NotNull
    private LocalDate dateOfBirth;

    private Gender gender;
    
    private String adharNumber;


    private String phone;
    private String email;
    private Integer admissionYear;
    private String admissionNumber; // optional - generated if missing
    private List<GuardianRef> guardians;
    // getters/setters
    private String currentClassId; // class mapping id or string like "10"
    private String currentSection; // A, B, etc.
    private String currentAcademicYear;

}
