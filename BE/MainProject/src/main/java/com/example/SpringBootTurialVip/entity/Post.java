package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@Table(name = "tbl_post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // Tiêu đề bài viết

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // Nội dung bài viết

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author; // Liên kết với User

    @Column(nullable = true)
    private String imageUrl; // Đường dẫn ảnh bài viết

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Post() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}


