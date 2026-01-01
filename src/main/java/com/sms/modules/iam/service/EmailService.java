package com.sms.modules.iam.service;

public interface EmailService {
    void send(String to, String subject, String body);
}