package com.sms.modules.student.domain;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Data;

@Data
public class GuardianRef {
    
    private String  adharNumber;
    private String name;
    private String relation; // father/mother/guardian
    private String phone;
    private String email;
    private String iamUserId; // optional link to IAM
    private boolean primary;
    // getters/setters
}