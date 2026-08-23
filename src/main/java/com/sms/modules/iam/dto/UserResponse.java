package com.sms.modules.iam.dto;

import com.sms.modules.iam.domain.RoleName;

import lombok.Data;

@Data
public class UserResponse {
    private String id;
    private String userId;
    private String fullName;
    private String email;
    private String mobile;
    private String profileImageUrl;
    private String status;
    private RoleName roleName;
    private String tenantId;
    private String adharNumber;
    // getters/setters
}