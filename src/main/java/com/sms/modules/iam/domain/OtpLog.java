package com.sms.modules.iam.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Document(collection = "otp_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpLog {
    @Id
    private String id;
    private String userId;
    private String otp;
    private Instant expiry;
    private boolean used;
    private String purpose; // PASSWORD_RESET / LOGIN_FALLBACK etc
    private String tenantId;

}