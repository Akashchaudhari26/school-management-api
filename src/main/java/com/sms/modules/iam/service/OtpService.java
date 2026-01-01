package com.sms.modules.iam.service;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sms.modules.iam.domain.OtpLog;
import com.sms.modules.iam.repository.OtpLogRepository;

@Service
public class OtpService {

    @Autowired
    private OtpLogRepository otpLogRepository;

    public String generateOtp(String userId, String purpose, long ttlSeconds) {
        Random r = new Random();
        int code = 100000 + r.nextInt(900000);
        String otp = Integer.toString(code);

        OtpLog o = new OtpLog();
        o.setUserId(userId);
        o.setOtp(otp);
        o.setPurpose(purpose);
        o.setUsed(false);
        o.setExpiry(Instant.now().plusSeconds(ttlSeconds));
        otpLogRepository.save(o);
        return otp;
    }

    public boolean validateOtp(String userId, String otp, String purpose) {
        Optional<OtpLog> o = otpLogRepository.findByUserIdAndOtpAndPurpose(userId, otp, purpose);
        if (o.isPresent()) {
            OtpLog log = o.get();
            if (!log.isUsed() && log.getExpiry().isAfter(Instant.now())) {
                log.setUsed(true);
                otpLogRepository.save(log);
                return true;
            }
        }
        return false;
    }
}