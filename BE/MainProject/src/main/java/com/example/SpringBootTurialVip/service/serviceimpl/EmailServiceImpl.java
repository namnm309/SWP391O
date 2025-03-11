package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.entity.OrderDetail;
import com.example.SpringBootTurialVip.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
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

    @Override
    public void sendVaccinationUpdateEmail(OrderDetail orderDetail) {
        String subject = "Cập nhật lịch tiêm chủng";
        String body = String.format(
                "Xin chào %s %s,\n\nLịch tiêm chủng của bạn đã được cập nhật:\n\n- Vaccine: %s\n- Ngày tiêm mới: %s\n\nVui lòng kiểm tra thông tin chi tiết trên hệ thống.",
                orderDetail.getFirstName(),
                orderDetail.getLastName(),
                orderDetail.getProduct().getTitle(),
                orderDetail.getVaccinationDate().toString()
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(orderDetail.getEmail());
        message.setSubject(subject);
        message.setText(body);

        emailSender.send(message);
    }
}
