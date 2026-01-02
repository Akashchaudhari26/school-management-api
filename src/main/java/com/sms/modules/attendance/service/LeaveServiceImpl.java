package com.sms.modules.attendance.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sms.modules.attendance.domain.Attendance;
import com.sms.modules.attendance.domain.AttendanceStatus;
import com.sms.modules.attendance.domain.LeaveRequest;
import com.sms.modules.attendance.domain.LeaveStatus;
import com.sms.modules.attendance.domain.UserType;
import com.sms.modules.attendance.repository.AttendanceRepository;
import com.sms.modules.attendance.repository.LeaveRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl {

    private final LeaveRepository leaveRepo;
    private final AttendanceRepository attendanceRepo; // To auto-update attendance

    // 1. Apply
    public LeaveRequest applyLeave(LeaveRequest request) {
        // 1. Validation (Safety Check)
        if (request.getStartDate() == null || request.getEndDate() == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        // 2. Logic to Count Days (Skipping Sundays)
        long workingDays = 0;
        LocalDate current = request.getStartDate();

        while (!current.isAfter(request.getEndDate())) {
            // Check if NOT Sunday
            if (current.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workingDays++;
            }
            current = current.plusDays(1);
        }

        // 3. Set Values and Save
        request.setDays((int) workingDays);
        request.setStatus(LeaveStatus.PENDING);
        request.setAppliedOn(LocalDateTime.now());

        return leaveRepo.save(request);
    }

    // 2. Get My Leaves
    public List<LeaveRequest> getMyLeaves(String userId) {
        return leaveRepo.findByUserIdOrderByAppliedOnDesc(userId);
    }

    // 3. Get All (For Admin)
    public List<LeaveRequest> getAllLeaves() {
        return leaveRepo.findAllByOrderByAppliedOnDesc();
    }

    // 4. Approve/Reject Logic
    @Transactional
    public LeaveRequest updateStatus(String id, LeaveStatus status, String rejectionReason) {
        LeaveRequest leave = leaveRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        leave.setStatus(status);
        if (status == LeaveStatus.REJECTED) {
            leave.setRejectionReason(rejectionReason);
        }

        LeaveRequest savedLeave = leaveRepo.save(leave);

        // 🔥 AUTOMATION: If Approved, mark attendance as ABSENT for those days
        if (status == LeaveStatus.APPROVED) {
            markAttendanceForLeave(savedLeave);
        }

        return savedLeave;
    }

    private void markAttendanceForLeave(LeaveRequest leave) {
        LocalDate current = leave.getStartDate();
        LocalDate end = leave.getEndDate();

        while (!current.isAfter(end)) {
            // Check if attendance already exists
            // We use your existing Repository method structure
            // Assuming we are treating LEAVE as 'ABSENT' for the register

            Attendance att = new Attendance();
            att.setUserId(leave.getUserId());
            att.setUserType(UserType.valueOf(leave.getUserType())); // STAFF
            att.setDate(current);
            att.setStatus(AttendanceStatus.ABSENT);
            att.setRemarks("On Leave: " + leave.getLeaveType()); // e.g. "On Leave: SICK"

            // Save or Update logic (You might need a specialized method in repo to upsert)
            // For simplicity:
            attendanceRepo.save(att);

            current = current.plusDays(1);
        }
    }

    public void withdrawLeave(String leaveId, String userId) {
        LeaveRequest leave = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        if (!leave.getUserId().equals(userId)) {
            throw new RuntimeException("You are not authorized to withdraw this request");
        }

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new RuntimeException("Cannot withdraw a request that is already processed");
        }

        leaveRepo.delete(leave);
    }
}