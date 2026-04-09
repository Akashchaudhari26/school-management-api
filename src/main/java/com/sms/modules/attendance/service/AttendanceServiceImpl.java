package com.sms.modules.attendance.service;

import com.sms.modules.attendance.domain.Attendance;
import com.sms.modules.attendance.domain.AttendanceStatus;
import com.sms.modules.attendance.domain.AttendanceSummaryStats;
import com.sms.modules.attendance.domain.UserType;
import com.sms.modules.attendance.dto.AttendanceCreateRequest;
import com.sms.modules.attendance.dto.AttendanceResponse;
import com.sms.modules.attendance.repository.AttendanceRepository;
import com.sms.modules.audit.domain.AuditAction;
import com.sms.modules.audit.domain.Auditable;
import com.sms.modules.staff.domain.Staff;
import com.sms.modules.staff.repository.StaffRepository;
import com.sms.modules.student.domain.Student;
import com.sms.modules.student.repository.StudentRepository;
import com.sms.security.SecurityUtils;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.access.AccessDeniedException;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

	private final AttendanceRepository attendanceRepo;
	private final StudentRepository studentRepo; // INJECT THIS
	private final StaffRepository staffRepo; // NEW INJECTION
	private final MongoTemplate mongoTemplate;

	@Auditable(action = AuditAction.MARK_ATTENDANCE, entity = "ATTENDANCE", captureOldValue = false)
	@Override
	@Transactional
	public AttendanceResponse markAttendance(AttendanceCreateRequest req, String markerId) {

		// 1. Validation: Check duplicates
		if (attendanceRepo.findByUserIdAndDate(req.getUserId(), req.getDate()).isPresent()) {
			throw new RuntimeException("Attendance already marked for this user on " + req.getDate());
		}

		// 2. Initialize Builder
		Attendance.AttendanceBuilder builder = Attendance.builder().userId(req.getUserId()).userType(req.getUserType())
				.date(req.getDate()).status(req.getStatus()).remarks(req.getRemarks()).markedBy(markerId);

		// 3. Context Switch Logic
		if (req.getUserType() == UserType.STUDENT) {
			enrichWithStudentData(builder, req.getUserId());
		} else if (req.getUserType() == UserType.STAFF) {
			enrichWithStaffData(builder, req.getUserId());
		} else {
			throw new IllegalArgumentException("Invalid User Type");
		}

		return toResponse(attendanceRepo.save(builder.build()));
	}

	// --- Helper: Student Data ---
	private void enrichWithStudentData(Attendance.AttendanceBuilder builder, String studentId) {
		Student student = studentRepo.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found: " + studentId));

		builder.nameSnapshot(student.getFirstName() + " " + student.getLastName()).classId(student.getCurrentClassId())
				.section(student.getCurrentSection()).currentAcademicYear(student.getCurrentAcademicYear());
		// Staff fields remain null
	}

	// --- Helper: Staff Data ---
	private void enrichWithStaffData(Attendance.AttendanceBuilder builder, String staffId) {
		Staff staff = staffRepo.findById(staffId)
				.orElseThrow(() -> new RuntimeException("Staff not found: " + staffId));

		builder.nameSnapshot(staff.getFullName()).designation(staff.getDesignation()) // Store context
				.staffType(staff.getStaffType()); // TEACHER or NON_TEACHING
		// Student fields (ClassId/Section) remain null
	}

	@Auditable(action = AuditAction.UPDATE_ATTENDANCE, entity = "ATTENDANCE", captureOldValue = true)
	@Override
	public AttendanceResponse updateAttendance(String id, AttendanceCreateRequest req, String userId) {
		Attendance a = attendanceRepo.findById(id).orElseThrow(() -> new RuntimeException("Attendance not found"));

		a.setStatus(req.getStatus());
		a.setRemarks(req.getRemarks());
		a.setMarkedBy(userId);

		attendanceRepo.save(a);
		return toResponse(a);
	}

	@Override
	public AttendanceResponse getAttendance(String id) {
		return attendanceRepo.findById(id).map(this::toResponse)
				.orElseThrow(() -> new RuntimeException("Attendance not found"));
	}

	@Override
	public Page<AttendanceResponse> getAttendanceByStudent(String studentId, Pageable pageable) {
		return attendanceRepo.findByUserId(studentId, pageable) // Fixed method call
				.map(this::toResponse);
	}

	@Override
	public Page<AttendanceResponse> getAttendanceByDate(LocalDate date, Pageable pageable) {
		return attendanceRepo.findByDate(date, pageable) // Fixed method call
				.map(this::toResponse);
	}

	@Auditable(action = AuditAction.DELETE_ATTENDANCE, entity = "ATTENDANCE", captureOldValue = true)
	@Override
	public void deleteAttendance(String id) {
		attendanceRepo.deleteById(id);
	}

	private AttendanceResponse toResponse(Attendance a) {
		return AttendanceResponse.builder().id(a.getId()).userId(a.getUserId()).date(a.getDate()).status(a.getStatus())
				.remarks(a.getRemarks()).markedBy(a.getMarkedBy()).createdAt(a.getCreatedAt())
				.updatedAt(a.getUpdatedAt()).userType(a.getUserType()).build();
	}

	@Auditable(action = AuditAction.MARK_BULK_ATTENDANCE, entity = "ATTENDANCE", captureOldValue = false)
	@Override
	@Transactional
	public List<AttendanceResponse> markBulkAttendance(List<AttendanceCreateRequest> reqList, String markerId) {
		if (reqList.isEmpty())
			return List.of();

		LocalDate date = reqList.get(0).getDate();

		// 1. Check duplicates globally for this batch
		Set<String> userIds = reqList.stream().map(AttendanceCreateRequest::getUserId).collect(Collectors.toSet());
		// 2. Optimized Fetching: Split IDs by Type
		Set<String> studentIds = reqList.stream().filter(r -> r.getUserType() == UserType.STUDENT)
				.map(AttendanceCreateRequest::getUserId).collect(Collectors.toSet());

		Set<String> staffIds = reqList.stream().filter(r -> r.getUserType() == UserType.STAFF)
				.map(AttendanceCreateRequest::getUserId).collect(Collectors.toSet());

		// 3. Batch Fetch from DB (Only 2 DB calls instead of N)
		Map<String, Student> studentMap = studentIds.isEmpty() ? Collections.emptyMap()
				: studentRepo.findAllById(studentIds).stream()
						.collect(Collectors.toMap(Student::getId, Function.identity()));

		Map<String, Staff> staffMap = staffIds.isEmpty() ? Collections.emptyMap()
				: staffRepo.findAllById(staffIds).stream().collect(Collectors.toMap(Staff::getId, Function.identity()));

		// 4. Map Requests to Entities
		List<Attendance> batch = reqList.stream().map(req -> {
			Attendance.AttendanceBuilder b = Attendance.builder().userId(req.getUserId()).userType(req.getUserType())
					.date(req.getDate()).status(req.getStatus()).remarks(req.getRemarks()).markedBy(markerId);

			if (req.getId() != null) {
				b.id(req.getId());
			}

			if (req.getUserType() == UserType.STUDENT) {
				Student s = studentMap.get(req.getUserId());
				if (s == null)
					throw new RuntimeException("Student ID " + req.getUserId() + " invalid");
				b.nameSnapshot(s.getFirstName() + " " + s.getLastName()).classId(s.getCurrentClassId())
						.section(s.getCurrentSection()).currentAcademicYear(s.getCurrentAcademicYear());
			} else {
				Staff s = staffMap.get(req.getUserId());
				if (s == null)
					throw new RuntimeException("Staff ID " + req.getUserId() + " invalid");
				b.nameSnapshot(s.getFullName()).designation(s.getDesignation()).staffType(s.getStaffType());
			}
			return b.build();
		}).toList();

		return attendanceRepo.saveAll(batch).stream().map(this::toResponse).toList();
	}

	// =================================================================
	// 1. CLASS VIEW IMPLEMENTATION
	// =================================================================
	@Override
	public List<AttendanceResponse> getAttendanceByClassAndSection(String classId, String sectionId, LocalDate date) {
		// Fetch all records for this specific class/section on the specific date
		List<Attendance> records = attendanceRepo.findByClassIdAndSectionAndDate(classId, sectionId, date);

		// Convert to Response DTOs
		return records.stream().map(this::toResponse).toList();
	}

	// =================================================================
	// 2. CALENDAR RANGE VIEW IMPLEMENTATION
	// =================================================================
	@Override
	public List<AttendanceResponse> getStudentAttendanceByRange(String studentId, LocalDate startDate,
			LocalDate endDate) throws AccessDeniedException {
		if (SecurityUtils.hasRole("PARENT")
				&& !studentRepo.existsByIdAndGuardiansIamUserId(studentId, SecurityUtils.getCurrentUserId())) {
			throw new AccessDeniedException("Not your child");
		}
		// Validation: Ensure valid range
		if (startDate.isAfter(endDate)) {
			throw new IllegalArgumentException("Start date cannot be after end date");
		}

		// Fetch records between dates (Inclusive)
		List<Attendance> records = attendanceRepo.findByUserIdAndDateBetween(studentId, startDate, endDate);

		return records.stream().map(this::toResponse).toList();
	}

	// =================================================================
	// 3. STAFF VIEW IMPLEMENTATION
	// =================================================================
	@Override
	public Page<AttendanceResponse> getStaffAttendance(String department, LocalDate date, Pageable pageable) {
		// Logic: If a department is provided, filter by it.
		// If NO department is provided, show ALL staff attendance.

		Page<Attendance> pageResult;

		if (department != null && !department.isBlank()) {
			// Filter by Dept (e.g., "Science")
			pageResult = attendanceRepo.findByDepartmentAndDate(department, date, pageable);
		} else {
			// Show all STAFF (Ignore Students)
			pageResult = attendanceRepo.findByUserTypeAndDate(UserType.STAFF, date, pageable);
		}

		return pageResult.map(this::toResponse);
	}

	@Override
	public AttendanceSummaryStats getDailyStats(UserType userType, LocalDate date) {

		// 1. Aggregation Pipeline
		Aggregation aggregation = newAggregation(
				// Match: Filter by Date and UserType
				match(Criteria.where("date").is(date).and("userType").is(userType)),

				// Group: Count by Status
				group("status").count().as("count"),

				// Project: Map '_id' (which is the status) to a field named 'status'
				project("count").and("_id").as("status"));

		// 2. Execute (Do not wrap in try-catch indiscriminately)
		AggregationResults<StatusCount> results = mongoTemplate.aggregate(aggregation, "attendance", StatusCount.class);

		// 3. Convert List to Map for easy lookup (Handles missing statuses
		// automatically)
		// Key: Status (PRESENT), Value: Count (15)
		java.util.Map<AttendanceStatus, Long> counts = results.getMappedResults().stream()
				.collect(java.util.stream.Collectors.toMap(StatusCount::getStatus, StatusCount::getCount));

		// 4. Extract Values (Default to 0 if not found)
		long present = counts.getOrDefault(com.sms.modules.attendance.domain.AttendanceStatus.PRESENT, 0L);
		long absent = counts.getOrDefault(com.sms.modules.attendance.domain.AttendanceStatus.ABSENT, 0L);
		long late = counts.getOrDefault(com.sms.modules.attendance.domain.AttendanceStatus.LATE, 0L);
		long halfDay = counts.getOrDefault(com.sms.modules.attendance.domain.AttendanceStatus.HALF_DAY, 0L);

		long totalMarked = present + absent + late + halfDay;

		// 5. Calculate Percentage (Prevent NaN)
		double percentage = (totalMarked == 0) ? 0.0 : ((double) (present + late) / totalMarked) * 100;
		if (userType.equals(UserType.STUDENT))
			return AttendanceSummaryStats.builder().totalStudents(totalMarked).present(present).absent(absent)
					.late(late).halfDay(halfDay).presencePercentage(Math.round(percentage * 10.0) / 10.0).build();
		return AttendanceSummaryStats.builder().totalStaff(totalMarked).present(present).absent(absent).late(late)
				.halfDay(halfDay).presencePercentage(Math.round(percentage * 10.0) / 10.0).build();
	}

	@Override
	public List<AttendanceResponse> getAttendanceForParent(String studentId) throws AccessDeniedException {
		// TODO Auto-generated method stub
		SecurityUtils.hasRole("PARENT");
		if (!studentRepo.existsByIdAndGuardiansIamUserId(studentId, SecurityUtils.getCurrentUserId())) {
			throw new AccessDeniedException("Not your child");
		}
		List<AttendanceResponse> attendance = attendanceRepo.findByUserId(studentId).stream().map(this::toResponse)
				.toList();
		return attendance;
	}

	@Override
	public List<AttendanceResponse> getClassAttendanceByRange(String classId, String sectionId, LocalDate startDate,
			LocalDate endDate) {
		// TODO Auto-generated method stub
		List<AttendanceResponse> monthlyAttendance = attendanceRepo
				.findByClassIdAndSectionIdAndDateBetween(classId, sectionId, startDate, endDate).stream()
				.map(this::toResponse).toList();
		return monthlyAttendance;
	}

	@Override
	public List<AttendanceResponse> getStaffAttendanceByRange(LocalDate startDate, LocalDate endDate) {
		return attendanceRepo
				.findByUserTypeAndDateBetween(UserType.STAFF, startDate, endDate) // Fetch only STAFF
				.stream()
				.map(this::toResponse)
				.toList();
	}
}

@Data
class StatusCount {
	private AttendanceStatus status;
	private long count;
}
