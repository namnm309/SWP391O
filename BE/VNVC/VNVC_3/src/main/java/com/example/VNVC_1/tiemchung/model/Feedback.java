package com.example.VNVC_1.tiemchung.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;  // Thêm trường này để lưu ID của người dùng

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private int rating;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
