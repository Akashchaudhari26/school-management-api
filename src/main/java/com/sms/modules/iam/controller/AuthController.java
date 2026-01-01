package com.sms.modules.iam.controller;

import com.sms.modules.iam.dto.*;
import com.sms.modules.iam.service.AuthService;
import com.sms.modules.iam.service.PasswordResetService;
import com.sms.modules.iam.service.OtpService;
import com.sms.modules.iam.service.EmailService;
import com.sms.modules.iam.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest req) {
        User u = authService.register(req);
        return ResponseEntity.ok(u);
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<?> requestPasswordReset(@RequestParam String email) {
        passwordResetService.createPasswordResetToken(email);
        return ResponseEntity.ok("Reset token sent (console/email)");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        boolean ok = passwordResetService.resetPassword(token, newPassword);
        if (ok) return ResponseEntity.ok("Password reset successful");
        return ResponseEntity.badRequest().body("Invalid or expired token");
    }

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@RequestParam String userId, @RequestParam String purpose) {
        String otp = otpService.generateOtp(userId, purpose, 300); // 5 minutes
        // zero budget: print
        System.out.println("OTP for user " + userId + " : " + otp);
        return ResponseEntity.ok("OTP generated and printed in server logs");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String userId, @RequestParam String otp, @RequestParam String purpose) {
        boolean ok = otpService.validateOtp(userId, otp, purpose);
        if (ok) return ResponseEntity.ok("OTP verified");
        return ResponseEntity.badRequest().body("Invalid/expired OTP");
    }
}
