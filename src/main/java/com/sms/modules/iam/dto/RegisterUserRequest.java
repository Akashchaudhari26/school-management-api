package com.sms.modules.iam.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import com.sms.modules.staff.domain.Staff;
import com.sms.modules.student.domain.GuardianRef;

@Data
public class RegisterUserRequest {

    // Existing fields
    private String adharNumber;
    private String fullName;
    private String email;
    private String password;
    private String mobile;
    private String roleName; // ADMIN / TEACHER / STUDENT / PARENT
    private String tenantId;
    private String userId;

    private String profileImageUrl;

    private String status;

    private List<String> permissions;

    private String classId;

    private String departmentId;

    public static RegisterUserRequest mapStaffToUserRequest(Staff staff) {
        RegisterUserRequest req = new RegisterUserRequest();

        // --- Basic Mapping ---
        req.setFullName(staff.getFullName());
        req.setEmail(staff.getEmail());
        req.setMobile(staff.getMobile());
        req.setAdharNumber(staff.getAdhaar()); // Note: Staff has 'adhaar', Req has 'adharNumber'
        req.setUserId(staff.getId());
        req.setTenantId(staff.getTenantId());
        req.setStatus("ACTIVE");

        String adhar = staff.getAdhaar();
        String last4 = (adhar != null && adhar.length() >= 4)
                ? adhar.substring(adhar.length() - 4)
                : "1234";

        req.setPassword("Staff@" + last4);

        // --- 🎭 ROLE MAPPING ---
        // Logic: If 'TEACHER', use TEACHER role.
        // For others, use the Designation (e.g., "PRINCIPAL", "ACCOUNTANT").
        String role = "STAFF"; // Default fallback

        if ("TEACHER".equalsIgnoreCase(staff.getDesignation())) {
            role = "TEACHER";
            // Map Department for teachers if applicable (e.g. Science, Maths)
            // You might pick the first subject or leave it blank
            if (staff.getSubjects() != null && !staff.getSubjects().isEmpty()) {
                req.setDepartmentId(staff.getSubjects().get(0));
            }
        } else if (staff.getDesignation() != null) {
            // Clean string: "Vice Principal" -> "VICE_PRINCIPAL"
            role = staff.getDesignation().trim().toUpperCase().replace(" ", "_");
        }

        req.setRoleName(role);

        return req;
    }

    public static RegisterUserRequest mapGuardianToUserRequest(GuardianRef guardian) {
        RegisterUserRequest req = new RegisterUserRequest();

        // --- Basic Mapping ---
        req.setFullName(guardian.getName());
        req.setEmail(guardian.getEmail());
        req.setMobile(guardian.getPhone());
        req.setAdharNumber(guardian.getAdharNumber());

        // User ID Logic: Parents don't have "Employee Codes".
        // Use Aadhaar Number as the User ID (it's unique).
        req.setUserId(guardian.getAdharNumber());

        req.setStatus("ACTIVE");
        req.setPermissions(new ArrayList<>()); // Initialize empty list

        // --- 🔐 PASSWORD LOGIC ---
        // Strategy: "Parent@" + Last 4 digits of Aadhaar
        // Example: Aadhaar "123456789012" -> Password "Parent@9012"
        String adhar = guardian.getAdharNumber();
        String last4 = (adhar != null && adhar.length() >= 4)
                ? adhar.substring(adhar.length() - 4)
                : "1234";

        req.setPassword("Parent@" + last4);

        // --- 🎭 ROLE MAPPING ---
        req.setRoleName("PARENT");

        // Parent specific: No ClassId or DepartmentId needed
        req.setClassId(null);
        req.setDepartmentId(null);

        return req;
    }
}