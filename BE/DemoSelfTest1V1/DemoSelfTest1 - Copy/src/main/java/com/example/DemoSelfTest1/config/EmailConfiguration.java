package com.example.DemoSelfTest1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration//định nghĩa ra các bean
public class EmailConfiguration {

    @Value("${spring.mail.username}")//tự config giá trị riêng và truyền vào biến  emailUsername
    private String emailUsername;

    @Value("${spring.mail.password}")//tự config giá trị riêng và truyền vào biến email
    private String emailPassword;

    @Bean//Khai báo đây là 1 Bean sẽ đc @Configuration đem vao Context Apllication
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(emailUsername);
        mailSender.setPassword(emailPassword);

        //Cấu hình email
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");//dùng giao thức smtp để send mail
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}