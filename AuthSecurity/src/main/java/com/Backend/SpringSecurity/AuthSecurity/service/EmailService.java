package com.Backend.SpringSecurity.AuthSecurity.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(String email, String otp) {
        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Email Verification OTP");
        message.setText(
                "Your OTP is: " + otp
                        + "\nIt expires in 5 minutes."
        );

        mailSender.send(message);
    }
}
