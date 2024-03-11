package com.example.backend.services.interfaces;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}

