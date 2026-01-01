package com.sms.modules.attendance.service;

import com.sms.modules.attendance.domain.AttendanceSummaryStats;
import com.sms.modules.attendance.domain.UserType;
import com.sms.modules.attendance.dto.AttendanceCreateRequest;
import com.sms.modules.attendance.dto.AttendanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

	AttendanceResponse markAttendance(AttendanceCreateRequest req, String userId);

	AttendanceResponse updateAttendance(String id, AttendanceCreateRequest req, String userId);

	AttendanceResponse getAttendance(String id);

	Page<AttendanceResponse> getAttendanceByStudent(String studentId, Pageable pageable);

	Page<AttendanceResponse> getAttendanceByDate(LocalDate date, Pageable pageable);

	void deleteAttendance(String id);

	List<AttendanceResponse> markBulkAttendance(List<AttendanceCreateRequest> req, String userId);

	// Add to AttendanceService.java interface

	// 1. For the Class View
	List<AttendanceResponse> getAttendanceByClassAndSection(String classId, String sectionId, LocalDate date);

	// 2. For the Calendar View (Range)
	List<AttendanceResponse> getStudentAttendanceByRange(String studentId, LocalDate startDate, LocalDate endDate) throws AccessDeniedException;

	// 3. For Staff View
	Page<AttendanceResponse> getStaffAttendance(String department, LocalDate date, Pageable pageable);

	// 4. For Dashboard Stats
	AttendanceSummaryStats getDailyStats(UserType userType, LocalDate date);

	List<AttendanceResponse> getAttendanceForParent(String studentId) throws AccessDeniedException;
	
	List<AttendanceResponse> getClassAttendanceByRange(String classId, String sectionId, LocalDate startDate, LocalDate endDate);
}
