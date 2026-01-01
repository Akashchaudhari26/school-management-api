package com.sms.modules.attendance.dto;

import com.sms.modules.attendance.domain.AttendanceStatus;
import com.sms.modules.attendance.domain.UserType;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceCreateRequest {
    private String id;
    private String userId;
    private UserType userType; // NEW: Mandatory field
    private LocalDate date;
    private AttendanceStatus status;
    private String remarks;
}