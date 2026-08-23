package com.sms.modules.iam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sms.modules.iam.domain.PasswordResetToken;
import com.sms.modules.iam.domain.User;
import com.sms.modules.iam.dto.ForgotPasswordRequest;
import com.sms.modules.iam.dto.PasswordResetResponse;
import com.sms.modules.iam.repository.PasswordResetTokenRepository;
import com.sms.modules.iam.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String RESET_RESPONSE = "If the account exists, the password has been reset successfully";

    public PasswordResetResponse resetPassword(ForgotPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirmation password must match");
        }

        String identifier = request.getIdentifier().trim();
        Optional<User> user = findUser(identifier);
        if (user.isEmpty()) {
            return new PasswordResetResponse(RESET_RESPONSE);
        }

        User account = user.get();
        Instant changedAt = Instant.ofEpochMilli(System.currentTimeMillis());
        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        account.setPasswordChangedAt(changedAt);
        account.setUpdatedAt(changedAt);
        userRepository.save(account);
        return new PasswordResetResponse(RESET_RESPONSE);
    }

    private Optional<User> findUser(String identifier) {
        Optional<User> user = userRepository.findByUserId(identifier);
        if (user.isPresent()) return user;

        user = userRepository.findByEmail(identifier.toLowerCase());
        if (user.isPresent()) return user;

        return userRepository.findByMobile(identifier);
    }

    public String createPasswordResetToken(String email) {
        User u = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        String token = UUID.randomUUID().toString();

        PasswordResetToken t = new PasswordResetToken();
        t.setUserId(u.getId());
        t.setToken(token);
        t.setExpiry(Instant.now().plusSeconds(60 * 60)); // 1 hour
        t.setUsed(false);
        tokenRepository.save(t);

        // For zero-budget just print to console or send email
        String link = "http://localhost:8080/auth/reset-password?token=" + token;
        emailService.send(u.getEmail(), "Password reset", "Use this link to reset password: " + link);
        return token;
    }

    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> ot = tokenRepository.findByToken(token);
        if (ot.isEmpty()) return false;
        PasswordResetToken prt = ot.get();
        if (prt.isUsed() || prt.getExpiry().isBefore(Instant.now())) return false;

        User u = userRepository.findById(prt.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        // update password
        u.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(u);

        prt.setUsed(true);
        tokenRepository.save(prt);
        return true;
    }
}
