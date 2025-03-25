package com.example.SpringBootTurialVip.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationSentDTO {
    private Long id;
    private String receiverUsername;
    private String receiverFullname;
    private String receiverAvatar;
    private String message;
    private LocalDateTime createdAt;
    private boolean readStatus;
}
