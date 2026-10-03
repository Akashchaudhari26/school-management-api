package com.sms.modules.iam.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.modules.iam.domain.OtpLog;

public interface OtpLogRepository extends JpaRepository<OtpLog, String> {
    Optional<OtpLog> findByUserIdAndOtpAndPurpose(String userId, String otp, String purpose);
}