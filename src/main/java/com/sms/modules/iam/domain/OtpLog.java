package com.sms.modules.iam.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "otp_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpLog {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private String id;
    private String userId;
    private String otp;
    private Instant expiry;
    private boolean used;
    private String purpose; // PASSWORD_RESET / LOGIN_FALLBACK etc
    private String tenantId;

}