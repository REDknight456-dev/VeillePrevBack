package com.cww.veille_springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send2FACode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("mahdi.bouassida69@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Your 2FA Verification Code");
        message.setText("Your 2FA code is: " + code);
        mailSender.send(message);
        System.out.println("Mail sent successfully!");
    }
}
