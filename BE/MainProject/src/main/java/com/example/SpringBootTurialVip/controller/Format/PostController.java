package com.example.SpringBootTurialVip.controller.Format;

import com.example.SpringBootTurialVip.entity.Post;
import com.example.SpringBootTurialVip.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Tag(name="[Post]",description = "")
public class PostController {

    @Autowired
    private PostService postService;

    // API Thêm bài viết (có ảnh)
    @Operation(summary = "API thêm bài viết", description =
            "Cho phép staff thêm bài viết mới, có thể kèm hình ảnh.\n"
                    + "Yêu cầu: gửi dưới dạng multipart/form-data."
    )
    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");

        return ResponseEntity.ok(postService.addPostWithImage(title, content, userId, image));
    }

    // API Lấy danh sách tất cả bài viết
    @Operation(summary = "API lấy danh sách bài viết", description =
            "Trả về danh sách tất cả bài viết trong hệ thống."
    )
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // API Lấy danh sách bài viết của 1 nhân viên cụ thể
    @Operation(summary = "API lấy danh sách bài viết của một nhân viên", description =
            "Trả về danh sách bài viết của nhân viên dựa trên staffId."
    )
    @GetMapping("/posts/staff/{staffId}")
    public ResponseEntity<List<Post>> getPostsByStaff(@PathVariable Long staffId) {
        return ResponseEntity.ok(postService.getPostsByStaff(staffId));
    }

    // API Cập nhật bài viết (có ảnh mới hoặc không)
    @PutMapping(value = "/posts/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "API cập nhật bài viết",
            description = "Cho phép staff cập nhật tiêu đề, nội dung bài viết và thay thế ảnh cũ nếu có."
    )
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile image) {

        try {
            // Gọi service để cập nhật bài viết
            Post updatedPost = postService.updatePost(id, title, content, image);

            return ResponseEntity.ok(Collections.singletonMap("message", "Post updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }



    // API Xóa bài viết
    @Operation(summary = "API xóa bài viết", description =
            "Xóa bài viết dựa trên ID bài viết. Hành động này không thể khôi phục."
    )
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted successfully");
    }
}
