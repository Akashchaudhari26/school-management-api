package com.sms.modules.staff.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import com.sms.modules.staff.domain.StaffStatus;

@Data
public class StaffCreateRequest {

    private String fullName;
    private String email;
    private String mobile;
    private String gender;
    private LocalDate dateOfBirth;
    private String aadhaar;

    private String staffType; // TEACHER / NON_TEACHING
    private String designation;
    private LocalDate joiningDate;
    private String employeeCode;

    private List<String> subjects;
    private List<String> assignedClassIds;
    
    private StaffStatus status;

    private String tenantId;
}
