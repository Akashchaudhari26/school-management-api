package com.sms.modules.attendance.dto;

import com.sms.modules.attendance.domain.AttendanceStatus;
import com.sms.modules.attendance.domain.UserType;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
public class AttendanceResponse {
	private String id;
	private String userId;
	private LocalDate date;
	private AttendanceStatus status;
	private String remarks;
	private String markedBy;
	private Instant createdAt;
	private Instant updatedAt;
	private UserType userType;
}
