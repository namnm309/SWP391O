package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private String content; // Nội dung bài viết , show 1 ít

    //Thêm nội dung chính
    @Column(columnDefinition = "TEXT", nullable = false)
    private String mainContent;


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

    @Column(nullable = true,length = 5000)
    private String imageUrl; // Đường dẫn ảnh bài viết

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Chỉ ánh xạ `id` của Category
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Chỉ lấy `name` của Category mà không lưu vào DB
    @Transient
    private String categoryName;

    // Phương thức để lấy `name` của Category từ đối tượng liên kết
    public String getCategoryName() {
        return category != null ? category.getName() : null;
    }

    // Chuyển đổi chuỗi imageUrls thành danh sách List<String>
    public List<String> getImageList() {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(imageUrl.split(",")); // Chuyển chuỗi thành danh sách
    }

    // Chuyển đổi danh sách List<String> thành chuỗi imageUrls
    public void setImageList(List<String> imageList) {
        if (imageList == null || imageList.isEmpty()) {
            this.imageUrl = null;
        } else {
            this.imageUrl = String.join(",", imageList); // Chuyển danh sách thành chuỗi
        }
    }


}
