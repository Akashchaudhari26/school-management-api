package com.sms.modules.iam.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sms.modules.iam.domain.OtpLog;

public interface OtpLogRepository extends MongoRepository<OtpLog, String> {
    Optional<OtpLog> findByUserIdAndOtpAndPurpose(String userId, String otp, String purpose);
}