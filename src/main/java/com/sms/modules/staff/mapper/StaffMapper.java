package com.sms.modules.staff.mapper;

import com.sms.modules.staff.domain.Staff;
import com.sms.modules.staff.dto.StaffCreateRequest;
import com.sms.modules.staff.dto.StaffResponse;

public class StaffMapper {

    public static Staff toEntity(StaffCreateRequest req) {
        return Staff.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .mobile(req.getMobile())
                .gender(req.getGender())
                .dateOfBirth(req.getDateOfBirth())
                .aadhaar(req.getAadhaar())
                .staffType(req.getStaffType())
                .designation(req.getDesignation())
                .joiningDate(req.getJoiningDate())
                .employeeCode(req.getEmployeeCode())
                .subjects(req.getSubjects())
                .assignedClassIds(req.getAssignedClassIds())
                .tenantId(req.getTenantId())
                .status(req.getStatus())
                .build();
    }

    public static StaffResponse toDto(Staff staff) {
        return StaffResponse.builder()
                .id(staff.getId())
                .fullName(staff.getFullName())
                .email(staff.getEmail())
                .mobile(staff.getMobile())
                .gender(staff.getGender())
                .dateOfBirth(staff.getDateOfBirth())
                .aadhaar(staff.getAadhaar())
                .staffType(staff.getStaffType())
                .designation(staff.getDesignation())
                .joiningDate(staff.getJoiningDate())
                .employeeCode(staff.getEmployeeCode())
                .subjects(staff.getSubjects())
                .assignedClassIds(staff.getAssignedClassIds())
                .tenantId(staff.getTenantId())
                .status(staff.getStatus())
                .build();
    }
}
