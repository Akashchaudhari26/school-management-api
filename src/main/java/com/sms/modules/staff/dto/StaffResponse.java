package com.sms.modules.staff.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import com.sms.modules.staff.domain.StaffStatus;

@Data
@Builder
public class StaffResponse {

    private String id;

    private String fullName;
    private String email;
    private String mobile;
    private String gender;
    private LocalDate dateOfBirth;
    private String adhaar;

    private String staffType;
    private String designation;
    private LocalDate joiningDate;
    private String employeeCode;

    private List<String> subjects;
    private List<String> assignedClassIds;

    private StaffStatus status;

    private String tenantId;
}
