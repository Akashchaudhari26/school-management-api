package com.sms.modules.attendance.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceSummaryStats {
    private long totalStudents;
    private long totalStaff;// Total strength (or total records found)
    private long present;        // Count of PRESENT
    private long absent;         // Count of ABSENT
    private long late;           // Count of LATE
    private long halfDay;        // Count of HALF_DAY
    private double presencePercentage; // e.g., 95.5
}