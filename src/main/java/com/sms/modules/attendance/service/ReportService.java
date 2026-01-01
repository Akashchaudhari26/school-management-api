package com.sms.modules.attendance.service;

import java.util.List;

import com.sms.modules.attendance.dto.AttendanceReportDTO;

public interface ReportService {
    
    // 1. Monthly Summary for a whole class
    List<AttendanceReportDTO> getClassMonthlyReport(String classId, String section, int month, int year);

    // 2. Students below a certain % threshold (Defaulters)
    List<AttendanceReportDTO> getDefaulters(String classId, String section, double threshold);

    // 3. Students absent for N consecutive days
    List<AttendanceReportDTO> getConsecutiveAbsentees(int days);
}