package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;

    public EmailServiceImpl() {
    }

    @Override
    public void sendVerificationEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = this.emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        this.emailSender.send(message);
    }
}
