package com.sms.modules.attendance.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "leave_requests")
@Data // Lombok
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {

    @Id
    private String id;

    private String userId;
    private String userName;
    private String userType; // "STAFF" or "STUDENT"
    private String role; // e.g. "Teacher"

    private LocalDate startDate;
    private LocalDate endDate;
    private int days;

    private LeaveType leaveType;
    private String reason;

    private LeaveStatus status = LeaveStatus.PENDING;

    @CreatedDate
    private LocalDateTime appliedOn;

    private String rejectionReason;
}