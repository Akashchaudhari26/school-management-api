package com.sms.modules.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sms.modules.attendance.domain.LeaveRequest;
import com.sms.modules.attendance.domain.LeaveStatus;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveRequest, String> {

    // For Staff: See their own history
    List<LeaveRequest> findByUserIdOrderByAppliedOnDesc(String userId);

    // For Admin: See pending requests
    List<LeaveRequest> findByStatus(LeaveStatus status);

    // For Admin: See all history (sorted by newest)
    List<LeaveRequest> findAllByOrderByAppliedOnDesc();
}