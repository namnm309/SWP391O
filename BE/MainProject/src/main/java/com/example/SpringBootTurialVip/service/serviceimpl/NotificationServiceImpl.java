package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.entity.Notification;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.repository.NotificationRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Notification sendOrderStatusNotification(Long userId, String orderStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String message = "Cập nhật đơn vaccine: Trạng thái đơn hàng của bạn hiện tại là '" + orderStatus + "'.";

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setReadStatus(true);
        notificationRepository.save(notification);
    }

    @Override
    @Scheduled(cron = "0 0 6 * * ?") // Chạy lúc 6h sáng mỗi ngày
    public void sendDailyVaccinationReminders() {
        List<User> customers = userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("CUSTOMER")))
                .collect(Collectors.toList());
        for (User user : customers) {
            sendNotification(user.getId(), "Nhắc nhở: Hôm nay bạn có lịch tiêm chủng cho bé. Vui lòng kiểm tra lịch hẹn!");
        }
    }

    //Gửi thông báo đến userId chỉ định
    @Override
    public Notification sendNotification(Long userId, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        return notificationRepository.save(notification);
    }

    @Override
    public void sendNotificationToAllStaff(String message) {
        List<User> staffUsers = userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("STAFF")))
                .collect(Collectors.toList());
        for (User user : staffUsers) {
            sendNotification(user.getId(), message);
        }
    }
}
