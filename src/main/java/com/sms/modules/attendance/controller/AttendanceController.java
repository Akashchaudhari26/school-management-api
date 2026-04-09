package com.sms.modules.attendance.controller;

import com.sms.modules.attendance.dto.AttendanceCreateRequest;
import com.sms.modules.attendance.dto.AttendanceResponse;
import com.sms.modules.attendance.domain.AttendanceSummaryStats; // Ensure you have this DTO
import com.sms.modules.attendance.domain.UserType;
import com.sms.modules.attendance.service.AttendanceService;
import com.sms.security.SecurityUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService svc;

    // =================================================================
    // 1. MARKING (Create & Update)
    // =================================================================

    @PreAuthorize("hasAuthority('ATTENDANCE_CREATE')")
    @PostMapping
    public ResponseEntity<AttendanceResponse> markAttendance(@RequestBody AttendanceCreateRequest req) {
        return ResponseEntity.ok(svc.markAttendance(req, SecurityUtils.getCurrentUser().getFullName()));
    }

    @PreAuthorize("hasAuthority('ATTENDANCE_CREATE')")
    @PostMapping("/bulk")
    public ResponseEntity<List<AttendanceResponse>> markBulkAttendance(@RequestBody List<AttendanceCreateRequest> req) {
        if (req == null || req.isEmpty()) {
            throw new IllegalArgumentException("Attendance list cannot be empty");
        }
        return ResponseEntity.ok(svc.markBulkAttendance(req, SecurityUtils.getCurrentUser().getFullName()));
    }

    @PreAuthorize("hasAuthority('ATTENDANCE_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<AttendanceResponse> update(@PathVariable String id,
            @RequestBody AttendanceCreateRequest req) {
        return ResponseEntity.ok(svc.updateAttendance(id, req, SecurityUtils.getCurrentUser().getFullName()));
    }

    // =================================================================
    // 2. CLASSROOM VIEWS (For Teachers)
    // =================================================================

    /**
     * "Show me the Class Register for 10-A on a specific date" Returns a List (not
     * Page) because teachers need to see the whole class at once.
     */
    @PreAuthorize("hasAnyAuthority('ATTENDANCE_READ', 'ATTENDANCE_READ_ALL')")
    @GetMapping("/class/{classId}/section/{sectionId}")
    public ResponseEntity<List<AttendanceResponse>> getClassAttendance(@PathVariable String classId,
            @PathVariable String sectionId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        // Default to today if no date provided
        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        return ResponseEntity.ok(svc.getAttendanceByClassAndSection(classId, sectionId, queryDate));
    }

    // =================================================================
    // 3. STUDENT/PARENT VIEWS (Calendar)
    // =================================================================

    /**
     * "Show me my attendance from Oct 1st to Oct 31st" (For Calendar UI)
     * 
     * @throws AccessDeniedException
     */
    @PreAuthorize("hasAnyAuthority('ATTENDANCE_READ', 'ATTENDANCE_READ_SELF_CHILD')")
    @GetMapping("/student/{studentId}/range")
    public ResponseEntity<List<AttendanceResponse>> getStudentAttendanceRange(@PathVariable String studentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate)
            throws AccessDeniedException {
        return ResponseEntity.ok(svc.getStudentAttendanceByRange(studentId, startDate, endDate));
    }

    // Generic paginated list for a student (Legacy/Admin use)
    @PreAuthorize("hasAuthority('ATTENDANCE_READ')")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Page<AttendanceResponse>> listByStudent(@PathVariable String studentId,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable p = PageRequest.of(page, size, Sort.by("date").descending());
        return ResponseEntity.ok(svc.getAttendanceByStudent(studentId, p));
    }

    // =================================================================
    // 4. STAFF VIEWS (For HR/Admin)
    // =================================================================

    /**
     * "Show me attendance for the Science Department" OR "Show me all Staff"
     */
    @PreAuthorize("hasAuthority('ATTENDANCE_READ')")
    @GetMapping("/staff")
    public ResponseEntity<Page<AttendanceResponse>> getStaffAttendance(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {

        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        Pageable p = PageRequest.of(page, size, Sort.by("nameSnapshot").ascending());

        return ResponseEntity.ok(svc.getStaffAttendance(department, queryDate, p));
    }

    // =================================================================
    // 5. ANALYTICS (Dashboard)
    // =================================================================

    /**
     * Dashboard Widget: "95% Present Today"
     */
    @PreAuthorize("hasAuthority('ATTENDANCE_READ')")
    @GetMapping("/stats")
    public ResponseEntity<AttendanceSummaryStats> getDailyStats(
            @RequestParam(defaultValue = "STUDENT") UserType userType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LocalDate queryDate = (date != null) ? date : LocalDate.now();
        return ResponseEntity.ok(svc.getDailyStats(userType, queryDate));
    }

    // =================================================================
    // 6. GENERAL UTILS
    // =================================================================

    @PreAuthorize("hasAuthority('ATTENDANCE_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<AttendanceResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(svc.getAttendance(id));
    }

    @PreAuthorize("hasAuthority('ATTENDANCE_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        svc.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ATTENDANCE_READ_SELF_CHILD')")
    @GetMapping("/parent/{studentId}")
    public ResponseEntity<List<AttendanceResponse>> attendance(@PathVariable String studentId)
            throws AccessDeniedException {

        return ResponseEntity.ok(svc.getAttendanceForParent(studentId));
    }

    @PreAuthorize("hasAuthority('ATTENDANCE_READ')")
    @GetMapping("/class/{classId}/section/{sectionId}/range")
    public ResponseEntity<List<AttendanceResponse>> getClassAttendanceByRange(@PathVariable String classId,
            @PathVariable String sectionId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(svc.getClassAttendanceByRange(classId, sectionId, startDate, endDate));
    }

    @PreAuthorize("hasAuthority('ATTENDANCE_READ')")
    @GetMapping("/staff/range")
    public ResponseEntity<List<AttendanceResponse>> getStaffAttendanceByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(svc.getStaffAttendanceByRange(startDate, endDate));
    }
}