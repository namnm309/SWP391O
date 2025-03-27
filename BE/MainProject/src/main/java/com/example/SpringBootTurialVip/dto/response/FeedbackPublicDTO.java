package com.example.SpringBootTurialVip.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackPublicDTO {
    private Long id;
    private String username;
    private String fullname;
    private int rating;
    private String comment;
    private String staffReply;
    private boolean replied;
    private LocalDateTime createdAt;
}
