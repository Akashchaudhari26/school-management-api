package com.sms.modules.attendance.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sms.modules.attendance.domain.Attendance;
import com.sms.modules.attendance.domain.AttendanceStatus;
import com.sms.modules.attendance.dto.AttendanceReportDTO;
import com.sms.modules.attendance.repository.AttendanceRepository;
import com.sms.modules.student.domain.GuardianRef;
import com.sms.modules.student.domain.Student;
import com.sms.modules.student.domain.StudentStatus;
import com.sms.modules.student.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final AttendanceRepository attendanceRepo;
    private final StudentRepository    studentRepository; // Use StudentRepository if you have one separate

    @Override
    public List<AttendanceReportDTO> getClassMonthlyReport(String classId, String section, int month, int year) {
        // 1. Define Date Range
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // 2. Fetch all students (Active only)
        List<Student> students = studentRepository.findByCurrentClassIdAndCurrentSectionAndStatus(
                classId, section, StudentStatus.ACTIVE);

        // 3. Fetch all attendance logs
        List<Attendance> attendance = attendanceRepo.findByClassIdAndSectionIdAndDateBetween(
                classId, section, startDate, endDate);

        // 4. Map logs by StudentID
        Map<String, List<Attendance>> logsByStudent = attendance.stream()
                .collect(Collectors.groupingBy(Attendance::getUserId));

        // --- LOGIC FIX: Calculate Class-Wide Working Days ---
        // Count unique dates in the logs. If attendance was taken on 22 days, this is 22.
        long classTotalWorkingDays = attendance.stream()
                .map(Attendance::getDate)
                .distinct()
                .count();

        List<AttendanceReportDTO> report = new ArrayList<>();

        for (Student student : students) {
            List<Attendance> studentLogs = logsByStudent.getOrDefault(student.getId(), new ArrayList<>());

            // Use equals() carefully if Enum vs String
            long present = studentLogs.stream().filter(a -> AttendanceStatus.PRESENT.equals(a.getStatus())).count();
            long absent  = studentLogs.stream().filter(a -> AttendanceStatus.ABSENT.equals(a.getStatus())).count();
            long late    = studentLogs.stream().filter(a -> AttendanceStatus.LATE.equals(a.getStatus())).count();

            // Use CLASS total as denominator (standard practice)
            // Or use studentLogs.size() only if you want "percentage of days MARKED"
            double percentage = (classTotalWorkingDays == 0) ? 0 : 
                                ((double) present / classTotalWorkingDays) * 100;

            // --- BUG FIX: Phone Number Extraction ---
            String phone = "N/A";
            if (student.getGuardians() != null && !student.getGuardians().isEmpty()) {
                phone = student.getGuardians().stream().filter(g -> g.isPrimary())
                        .map(GuardianRef::getPhone)
                        .filter(Objects::nonNull)
                        .findFirst()       // Get the first valid number
                        .orElse("N/A");
            }

            report.add(AttendanceReportDTO.builder()
                    .studentId(student.getId())
                    .studentName(student.getFirstName() + " " + student.getLastName())
                    .admissionNumber(student.getAdmissionNumber())
                    .totalWorkingDays(classTotalWorkingDays) // Fixed logic
                    .presentDays(present)
                    .absentDays(absent)
                    .lateDays(late)
                    .percentage(Math.round(percentage * 10.0) / 10.0)
                    .parentPhone(phone) // Fixed bug
                    .build());
        }

        return report;
    }
    @Override
    public List<AttendanceReportDTO> getDefaulters(String classId, String section, double threshold) {
	// Reuse logic: Get report for CURRENT month (or academic year up to now)
	// For simplicity, let's look at the current month
	LocalDate		  now	       = LocalDate.now();
	List<AttendanceReportDTO> monthlyStats = getClassMonthlyReport(classId, section, now.getMonthValue(),
		now.getYear());

	return monthlyStats.stream().filter(dto -> dto.getPercentage() < threshold).collect(Collectors.toList());
    }

    @Override
    public List<AttendanceReportDTO> getConsecutiveAbsentees(int consecutiveDays) {
        // 1. DETERMINING THE "LAST N WORKING DAYS"
        // We fetch logs from the last 2 weeks to be safe (handles weekends/holidays)
        LocalDate bufferDate = LocalDate.now().minusDays(14);
        List<Attendance> recentLogs = attendanceRepo.findByDateAfter(bufferDate);

        // Extract unique dates from logs and sort them (Newest to Oldest)
        List<LocalDate> lastWorkingDays = recentLogs.stream()
                .map(Attendance::getDate)
                .distinct()
                .sorted(Comparator.reverseOrder()) // Latest date first
                .limit(consecutiveDays) // Take only the requested N days
                .collect(Collectors.toList());

        // If we don't have enough data (e.g., school just opened), return empty
        if (lastWorkingDays.size() < consecutiveDays) {
            return new ArrayList<>();
        }

        // 2. IDENTIFY CONSECUTIVE ABSENTEES
        // Filter logs to only keep the target dates and ABSENT status
        List<Attendance> targetLogs = recentLogs.stream()
                .filter(log -> lastWorkingDays.contains(log.getDate()))
                .filter(log -> AttendanceStatus.ABSENT.equals(log.getStatus()))
                .collect(Collectors.toList());

        // Group by Student ID -> List of Absent Logs
        Map<String, List<Attendance>> absentLogsByStudent = targetLogs.stream()
                .collect(Collectors.groupingBy(Attendance::getUserId));

        // Find students who were absent on *ALL* target days
        List<String> defaulterIds = absentLogsByStudent.entrySet().stream()
                .filter(entry -> {
                    // Unique dates this student was absent matches the required days
                    long uniqueAbsentDays = entry.getValue().stream()
                            .map(Attendance::getDate)
                            .distinct()
                            .count();
                    return uniqueAbsentDays == consecutiveDays;
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (defaulterIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 3. FETCH STUDENT DETAILS & BUILD REPORT
        // Fetch only the identified students
        List<Student> defaulters = (List<Student>) studentRepository.findAllById(defaulterIds);

        return defaulters.stream().map(student -> {
            // Safe Phone Extraction
            String phone = "N/A";
            if (student.getGuardians() != null && !student.getGuardians().isEmpty()) {
                phone = student.getGuardians().stream()
                        .map(GuardianRef::getPhone)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse("N/A");
            }

            return AttendanceReportDTO.builder()
                    .studentId(student.getId())
                    .studentName(student.getFirstName() + " " + student.getLastName())
                    .admissionNumber(student.getAdmissionNumber())
                    .totalWorkingDays(consecutiveDays) // In this context, working days = the streak checked
                    .presentDays(0) // By definition, they were present 0 times in this streak
                    .absentDays(consecutiveDays)
                    .percentage(0.0)
                    .parentPhone(phone)
                    .build();
        }).collect(Collectors.toList());
    }
}