package com.sms.modules.attendance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sms.modules.attendance.dto.AttendanceReportDTO;
import com.sms.modules.attendance.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // 1. Monthly Class Summary
    @GetMapping("/summary/class")
    public ResponseEntity<List<AttendanceReportDTO>> getClassMonthlySummary(
            @RequestParam String classId,
            @RequestParam String section,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(reportService.getClassMonthlyReport(classId, section, month, year));
    }

    // 2. Short Attendance (Defaulters)
    @GetMapping("/defaulters")
    public ResponseEntity<List<AttendanceReportDTO>> getDefaulters(
            @RequestParam String classId,
            @RequestParam String section,
            @RequestParam(defaultValue = "75") double threshold) {
        return ResponseEntity.ok(reportService.getDefaulters(classId, section, threshold));
    }

    // 3. Consecutive Absentees
    @GetMapping("/consecutive-absent")
    public ResponseEntity<List<AttendanceReportDTO>> getConsecutiveAbsentees(
            @RequestParam(defaultValue = "3") int days) {
        return ResponseEntity.ok(reportService.getConsecutiveAbsentees(days));
    }
}