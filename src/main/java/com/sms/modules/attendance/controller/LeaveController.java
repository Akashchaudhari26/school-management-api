package com.sms.modules.attendance.controller;

import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sms.modules.attendance.domain.LeaveRequest;
import com.sms.modules.attendance.domain.LeaveStatus;
import com.sms.modules.attendance.service.LeaveServiceImpl;
import com.sms.modules.iam.domain.User;
import com.sms.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveServiceImpl leaveService;

    // 1. Apply
    @PostMapping("/apply")
    @PreAuthorize("hasAuthority('LEAVE_APPLY')") // Staff/Teachers
    public ResponseEntity<LeaveRequest> applyLeave(@RequestBody LeaveRequest request) {
        return ResponseEntity.ok(leaveService.applyLeave(request));
    }

    // 2. Get My Leaves
    @GetMapping("/my/{userId}")
    @PreAuthorize("#userId == authentication.principal.userId") // Security check
    public ResponseEntity<List<LeaveRequest>> getMyLeaves(@PathVariable String userId) {
        return ResponseEntity.ok(leaveService.getMyLeaves(userId));
    }

    // 3. Admin: Get All
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('LEAVE_APPROVE')") // Principal/Admin
    public ResponseEntity<List<LeaveRequest>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }

    // 4. Admin: Approve/Reject
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('LEAVE_APPROVE')")
    public ResponseEntity<LeaveRequest> updateStatus(
            @PathVariable String id,
            @RequestParam LeaveStatus status,
            @RequestParam(required = false) String reason) {

        return ResponseEntity.ok(leaveService.updateStatus(id, status, reason));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('LEAVE_APPLY')")
    public ResponseEntity<?> withdrawLeave(
            @PathVariable String id) { // Get logged-in user

        // Extract User ID (assuming you store it in Principal/Details)
        // If your Principal is the User object:
        User currentUser = SecurityUtils.getCurrentUser();
        leaveService.withdrawLeave(id, currentUser.getUserId());

        return ResponseEntity.ok(Map.of("message", "Leave request withdrawn successfully"));
    }
}