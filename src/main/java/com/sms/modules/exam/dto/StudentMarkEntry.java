package com.sms.modules.exam.dto;

import lombok.Data;

@Data
public class StudentMarkEntry {
    private String admissionNumber;
    private String studentId;
    private String studentName;
    private Double marksObtained; // The score
    private String remarks; // "Excellent", "Needs Improvement"
}