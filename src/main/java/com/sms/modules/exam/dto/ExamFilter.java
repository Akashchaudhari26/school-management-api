package com.sms.modules.exam.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamFilter {
    private String classId;
    private String section;
    private String academicYear;
    private String examName;
    private String subjectName;
    private String studentId; // Optional: For single student reports
}