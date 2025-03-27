package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.entity.OrderDetail;
import com.example.SpringBootTurialVip.entity.ProductOrder;
import jakarta.mail.MessagingException;

import java.util.List;

public interface EmailService {
    public void sendVerificationEmail(String to, String subject, String text) throws MessagingException;

    public void sendVaccinationUpdateEmail(OrderDetail orderDetail);

    public void sendCustomEmail(String to, String subject, String body);

    public void sendCancelOrderEmailWithReason(ProductOrder order, List<OrderDetail> details);


    }
