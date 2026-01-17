package com.sms.modules.student.domain;

import lombok.Data;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "student_academic_history")
public class StudentAcademicHistory {
    @Id
    private String id;

    private String studentId;
    private String academicYear; // The year they just finished (e.g. 2025-2026)
    private String classId; // e.g. NURSERY
    private String section; // e.g. A
    private String result; // PROMOTE, DEMOTE, RETAIN

    private long promotedAt; // Timestamp
    private String promotedBy; // User ID of admin
}