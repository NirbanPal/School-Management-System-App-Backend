package com.example.SMSApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String emailSub, String emailBody){

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(emailSub);
            message.setText("Your OTP is: " + emailBody);
            mailSender.send(message);

    }
}
