package com.example.SpringBootTurialVip.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateRequest {
        private String title;
        private String content;
        private String imageUrl; // Nếu muốn cập nhật ảnh
}
