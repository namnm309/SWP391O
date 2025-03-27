package com.example.SpringBootTurialVip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDTO {
    private Long id;
    private String message;
    private boolean readStatus;
    private LocalDateTime createdAt;
    private SenderDTO sender;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SenderDTO {
        private Long id;
        private String fullName;
    }
}
