package com.sms.modules.attendance.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "leave_requests")
@Data // Lombok
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {

    @Id

    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;
    private String userName;
    private String userType; // "STAFF" or "STUDENT"
    private String role; // e.g. "Teacher"

    private LocalDate startDate;
    private LocalDate endDate;
    private int days;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;
    private String reason;

    @Enumerated(EnumType.STRING)
    private LeaveStatus status = LeaveStatus.PENDING;

    @CreatedDate
    private LocalDateTime appliedOn;

    private String rejectionReason;
}
