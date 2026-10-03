package com.sms.modules.exam.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exam_results")
// Index for fast Report Card generation: Find all marks for Student X in Year Y
public class ExamResult {

    @Id

    @GeneratedValue(strategy = GenerationType.UUID)
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