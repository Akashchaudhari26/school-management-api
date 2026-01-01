package com.sms.modules.iam.service;

import org.springframework.stereotype.Service;

@Service
public class ConsoleEmailServiceImpl implements EmailService {
    @Override
    public void send(String to, String subject, String body) {
        System.out.println("---- Sent Email (console) ----");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        System.out.println("------------------------------");
    }
}