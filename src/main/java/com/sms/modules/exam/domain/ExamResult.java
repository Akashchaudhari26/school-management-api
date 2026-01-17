package com.sms.modules.exam.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "exam_results")
// Index for fast Report Card generation: Find all marks for Student X in Year Y
@CompoundIndex(name = "student_year_idx", def = "{'studentId': 1, 'academicYear': 1}")
public class ExamResult {

    @Id
    private String id;

    private String studentId; // Link to Student
    private String academicYear; // e.g., "2025-2026"
    private String classId; // Class they were in when taking the exam

    private String examName; // e.g., "Half Yearly", "Unit Test 1"
    private String subjectName; // e.g., "Mathematics", "Science"

    private double marksObtained; // e.g., 85.5
    private double totalMarks; // e.g., 100.0
    private String grade; // e.g., "A", "B+"

    private String remarks; // Teacher's specific comment
}