package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.entity.Category;
import com.example.SpringBootTurialVip.entity.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorName;
    @JsonIgnore
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> imageList; // Trả về dạng danh sách cho frontend
    private Category category;

    // Constructor chuyển từ Post sang PostResponse
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorId = post.getAuthor().getId();
        this.authorName = post.getAuthor().getFullname();
        this.imageUrl = post.getImageUrl();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.imageList = convertStringToList(post.getImageUrl()); // Chuyển đổi chuỗi thành danh sách
        this.category=post.getCategory();
    }

    // Chuyển đổi chuỗi imageUrls thành danh sách List<String>
    private List<String> convertStringToList(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(imageUrl.split(",")); // Chuyển chuỗi thành danh sách
    }
}
