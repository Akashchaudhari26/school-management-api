package com.sms.modules.iam.dto;

import java.util.List;

import lombok.Data;

@Data
public class CreateRoleRequest {
    private String name;
    private String description;
    private List<String> permissions;
    private boolean defaultRole;
    private String tenantId;
    // getters/setters
}