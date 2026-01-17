package com.sms.modules.exam.dto;

import lombok.Data;
import java.util.List;

@Data
public class BulkMarksRequest {
    private String classId; // e.g. "CLASS_1"
    private String section; // e.g. "A"
    private String academicYear; // e.g. "2025-2026"
    private String examName; // e.g. "MID_TERM"
    private String subjectName; // e.g. "MATHS"
    private Double totalMarks; // e.g. 100.0

    private List<StudentMarkEntry> studentMarks;
}