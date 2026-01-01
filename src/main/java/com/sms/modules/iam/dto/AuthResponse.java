package com.sms.modules.iam.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private long expiresIn;

    public AuthResponse() {}
    // getters/setters
}