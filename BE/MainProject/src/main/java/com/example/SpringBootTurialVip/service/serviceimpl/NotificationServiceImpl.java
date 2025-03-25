package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.response.NotificationResponseDTO;
import com.example.SpringBootTurialVip.dto.response.NotificationSentDTO;
import com.example.SpringBootTurialVip.entity.Notification;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.repository.NotificationRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người gửi"));
    }


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
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setSender(getCurrentUser());
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



    @Override
    public void sendNotificationToAllCustomers(String message) {
        List<User> customers = userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("CUSTOMER")))
                .toList();

        for (User user : customers) {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setMessage(message);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setSender(getCurrentUser());
            notificationRepository.save(notification);
        }
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findUnreadNotifications(userId);
        for (Notification notification : notifications) {
            notification.setReadStatus(true);
        }
        notificationRepository.saveAll(notifications);
    }

    @Override
    public List<Notification> getNotificationsSentBy(Long senderId) {
        return notificationRepository.findBySenderIdOrderByCreatedAtDesc(senderId);
    }

    @Override
    public List<NotificationSentDTO> getNotificationsSentByPublic(Long senderId) {
        List<Notification> list = notificationRepository.findBySenderId(senderId);
        return list.stream().map(notification -> {
            NotificationSentDTO dto = new NotificationSentDTO();
            dto.setId(notification.getId());
            dto.setReceiverUsername(notification.getUser().getUsername());
            dto.setReceiverFullname(notification.getUser().getFullname());
            dto.setReceiverAvatar(notification.getUser().getAvatarUrl());
            dto.setMessage(notification.getMessage());
            dto.setCreatedAt(notification.getCreatedAt());
            dto.setReadStatus(notification.isReadStatus());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public NotificationResponseDTO convertToDto(Notification notification) {
        NotificationResponseDTO.SenderDTO senderDTO = null;
        if (notification.getSender() != null) {
            senderDTO = new NotificationResponseDTO.SenderDTO(
                    notification.getSender().getId(),
                    notification.getSender().getFullname()
            );
        }

        return new NotificationResponseDTO(
                notification.getId(),
                notification.getMessage(),
                notification.isReadStatus(),
                notification.getCreatedAt(),
                senderDTO
        );
    }


}
