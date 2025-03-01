package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Liên kết với bảng tbl_users
    private User user;

    @Column(nullable = false)
    private int rating; // Số sao từ 1 đến 5

    @Column(nullable = false, length = 1000)
    private String comment; // Nội dung đánh giá

    @Column(nullable = true, length = 1000)
    private String staffReply; // Phản hồi của nhân viên

    @Column(nullable = false)
    private boolean replied = false; // Trạng thái phản hồi

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

//    public Feedback() {
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//    }


}

