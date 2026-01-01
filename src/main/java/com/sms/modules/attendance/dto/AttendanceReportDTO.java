package com.sms.modules.attendance.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceReportDTO {
    private String studentId;
    private String studentName;
    private String admissionNumber;
    private String rollNumber;
    
    // Stats
    private long totalWorkingDays;
    private long presentDays;
    private long absentDays;
    private long lateDays;
    private double percentage;
    
    // Contact (For the call list)
    private String parentPhone;
}