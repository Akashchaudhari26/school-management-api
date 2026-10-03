package com.sms.modules.student.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class GuardianRef {

    private String adharNumber;
    private String name;
    private String relation; // father/mother/guardian
    private String phone;
    private String email;
    private String iamUserId; // optional link to IAM
    @Column(name = "is_primary")
    private boolean primary;
    // getters/setters
}
