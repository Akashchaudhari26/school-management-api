package com.sms.modules.iam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sms.modules.iam.domain.PasswordResetToken;
import com.sms.modules.iam.domain.User;
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
        u.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(u);

        prt.setUsed(true);
        tokenRepository.save(prt);
        return true;
    }
}
