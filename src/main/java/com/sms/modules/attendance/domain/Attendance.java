package com.sms.modules.attendance.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("attendance")
@CompoundIndex(name = "user_date_idx", def = "{'userId': 1, 'date': 1}", unique = true)
public class Attendance {

    @Id
    private String id;

    @Indexed
    private String userId; // Can be StudentID or StaffID

    @Indexed
    private UserType userType; // NEW: ENUM (STUDENT, STAFF)

    // --- Student Specific Context ---
    private String classId;   // Null for Staff
    private String section;   // Null for Staff
    private String currentAcademicYear;

    // --- Staff Specific Context ---
    private String department; // e.g., "Accounts", "Physics Dept"
    private String designation; // e.g., "Sr. Accountant"

    // --- Common ---
    private LocalDate date;
    private AttendanceStatus status;
    private String remarks;
    private String markedBy;
    private String nameSnapshot; // "John Doe"

    private Instant createdAt;
    private Instant updatedAt;
    
    private String staffType;
}
