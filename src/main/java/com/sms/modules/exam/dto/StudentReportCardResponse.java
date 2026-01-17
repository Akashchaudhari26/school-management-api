package com.sms.modules.exam.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentReportCardResponse {
    // Header Info
    private String studentName;
    private String admissionNumber;
    private String className;
    private String section;
    private String academicYear;
    private String examName; // e.g. "Annual Exam"

    // The Marks List
    private List<SubjectMark> subjects;

    // Summary
    private Double totalMarksObtained;
    private Double maxTotalMarks;
    private Double percentage;
    private String finalGrade;
    private String resultStatus; // PASS / FAIL
}