package com.sms.modules.iam.dto;

import java.util.List;

import com.sms.modules.iam.domain.RoleName;

import lombok.Data;

@Data
public class CreateRoleRequest {
    private RoleName name;
    private String description;
    private List<String> permissions;
    private boolean defaultRole;
    private String tenantId;
    // getters/setters
}