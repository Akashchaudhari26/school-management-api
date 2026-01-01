package com.sms.modules.iam.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String mobile;
    private String adharNumber;
    private String password;
    private String tenantId; // optional for future

    public LoginRequest() {}
}
