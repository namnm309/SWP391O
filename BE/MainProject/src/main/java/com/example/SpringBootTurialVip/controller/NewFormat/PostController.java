package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.response.PostResponse;
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
import java.util.stream.Collectors;

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
    public ResponseEntity<PostResponse> createPost(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");

        // Gọi service để tạo bài viết
        Post post = postService.addPostWithImage(title, content, userId, image);

        // Chuyển đổi từ Post sang PostResponse
        PostResponse postResponse = new PostResponse(post);

        return ResponseEntity.ok(postResponse);
    }


    // API Lấy danh sách tất cả bài viết
    @Operation(summary = "API lấy danh sách bài viết", description =
            "Trả về danh sách tất cả bài viết trong hệ thống."
    )

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();

        // Chuyển đổi danh sách Post sang PostResponse để chỉ lấy thông tin cần thiết
        List<PostResponse> postResponses = posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(postResponses);
    }

    // API Lấy danh sách bài viết của 1 nhân viên cụ thể
    @Operation(summary = "API lấy danh sách bài viết của một nhân viên", description =
            "Trả về danh sách bài viết của nhân viên dựa trên staffId."
    )
    @GetMapping("/posts/staff/{staffId}")
    public ResponseEntity<List<PostResponse>> getPostsByStaff(@PathVariable Long staffId) {
        List<Post> posts = postService.getPostsByStaff(staffId);

        // Chuyển đổi danh sách Post sang PostResponse để chỉ lấy thông tin cần thiết
        List<PostResponse> postResponses = posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(postResponses);
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

    @Operation(summary = "API tìm kiếm bài viết", description =
            "Tìm bài viết theo ID hoặc tên bài viết (tìm gần đúng)."
    )
    @GetMapping("/posts/search")
    public ResponseEntity<List<PostResponse>> searchPosts(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String title) {

        List<Post> posts;

        if (id != null) {
            // Tìm theo ID
            Post post = postService.getPostById(id);
            posts = post != null ? List.of(post) : List.of();
        } else if (title != null && !title.isEmpty()) {
            // Tìm theo tên gần đúng
            posts = postService.searchByTitle(title);
        } else {
            // Nếu không có ID hoặc title, trả về toàn bộ danh sách
            posts = postService.getAllPosts();
        }

        // Chuyển đổi sang DTO để chỉ lấy thông tin cần thiết
        List<PostResponse> postResponses = posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(postResponses);
    }

}
