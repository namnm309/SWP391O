package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

    // Chỉ lưu `id` của User (tác giả)
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "user_id", nullable = false)
    private User author;

    // Chỉ lấy fullname của tác giả mà không lưu vào DB
    @Transient
    private String authorName;

    public String getAuthorName() {
        return author != null ? author.getFullname() : null;
    }

    @Column(nullable = true)
    private String imageUrl; // Đường dẫn ảnh bài viết

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
