package com.fundoo.notes.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendPasswordResetEmail(String toEmail, String resetLink);
}
