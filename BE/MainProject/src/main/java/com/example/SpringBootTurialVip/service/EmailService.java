package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.entity.OrderDetail;
import jakarta.mail.MessagingException;

public interface EmailService {
    public void sendVerificationEmail(String to, String subject, String text) throws MessagingException;

    public void sendVaccinationUpdateEmail(OrderDetail orderDetail);

    public void sendCustomEmail(String to, String subject, String body);

}
