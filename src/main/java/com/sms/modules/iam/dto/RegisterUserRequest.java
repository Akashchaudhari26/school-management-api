package com.sms.modules.iam.dto;

import lombok.Data;

@Data
public class RegisterUserRequest {
    private String adharNumber;
    private String fullName;
    private String email;
    private String password;
    private String mobile;
    private String roleName; // ADMIN/TEACHER/...
    private String tenantId;
    // getters/setters
}
