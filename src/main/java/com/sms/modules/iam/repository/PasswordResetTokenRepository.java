package com.sms.modules.iam.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sms.modules.iam.domain.PasswordResetToken;

public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByToken(String token);
}