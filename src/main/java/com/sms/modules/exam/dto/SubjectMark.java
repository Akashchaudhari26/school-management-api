package com.sms.modules.exam.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubjectMark {
    private String subjectName;
    private Double marksObtained;
    private Double totalMarks;
    private String grade;
    private String remarks;
}