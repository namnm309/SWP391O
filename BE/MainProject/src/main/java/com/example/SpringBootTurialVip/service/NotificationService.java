package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.entity.Notification;
import com.example.SpringBootTurialVip.entity.User;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.stream.Collectors;

public interface NotificationService {
    // Staff gửi thông báo đến Customer
    public Notification sendOrderStatusNotification(Long userId, String orderStatus);

    // Lấy danh sách thông báo của người dùng
    public List<Notification> getUserNotifications(Long userId);

    // Đánh dấu thông báo đã đọc
    public void markAsRead(Long notificationId);

    // Tự động gửi thông báo nhắc lịch tiêm lúc 7h sáng mỗi ngày
    public void sendDailyVaccinationReminders();

    //Admin gửi thông báo đến toàn staff
    public void sendNotificationToAllStaff(String message);

    //Gửi thông báo
    public Notification sendNotification(Long userId, String message);

    // Gửi thông báo đến tất cả user có role CUSTOMER
    public void sendNotificationToAllCustomers(String message);

    public void markAllAsRead(Long userId);


}
