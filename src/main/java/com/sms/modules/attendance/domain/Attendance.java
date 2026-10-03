package com.sms.modules.attendance.domain;

import lombok.*;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attendance", uniqueConstraints = @jakarta.persistence.UniqueConstraint(name = "uk_attendance_user_date", columnNames = {
        "user_id", "date" }))
public class Attendance {

    @Id

    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private String id;
    private String userId; // Can be StudentID or StaffID
    @jakarta.persistence.Enumerated(jakarta.persistence.EnumType.STRING)
    private UserType userType; // NEW: ENUM (STUDENT, STAFF)

    // --- Student Specific Context ---
    private String classId; // Null for Staff
    private String section; // Null for Staff
    private String currentAcademicYear;

    // --- Staff Specific Context ---
    private String department; // e.g., "Accounts", "Physics Dept"
    private String designation; // e.g., "Sr. Accountant"

    // --- Common ---
    private LocalDate date;
    @Enumerated(jakarta.persistence.EnumType.STRING)
    private AttendanceStatus status;
    private String remarks;
    private String markedBy;
    private String nameSnapshot; // "John Doe"

    private Instant createdAt;
    private Instant updatedAt;

    private String staffType;
}
