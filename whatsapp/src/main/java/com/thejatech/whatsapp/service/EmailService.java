package com.thejatech.whatsapp.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void sendEmailWithTemplate(String to, String subject, java.util.Map<String, String> placeholders);
}
