package com.pagepal.capstone.services;

public interface EmailService {

    void sendSimpleEmail(String toEmail,
                         String subject,
                         String body
    );
}
