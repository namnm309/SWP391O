package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@Table(name = "tbl_notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Liên kết với tbl_users
    private User user;

    @Column(nullable = false, length = 500)
    private String message; // Nội dung thông báo

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean readStatus = false; // Đánh dấu đã đọc

    public Notification() {
        this.createdAt = LocalDateTime.now();
    }

}

