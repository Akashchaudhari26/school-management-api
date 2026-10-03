package com.sms.modules.student.domain;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "student_academic_history")
public class StudentAcademicHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String studentId;
    private String academicYear; // The year they just finished (e.g. 2025-2026)
    private String classId; // e.g. NURSERY
    private String section; // e.g. A
    private String result; // PROMOTE, DEMOTE, RETAIN

    private long promotedAt; // Timestamp
    private String promotedBy; // User ID of admin
}
