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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN','ROLE_ROLE_STAFF')")
    @Operation(summary = "API thêm bài viết", description =
            "Cho phép staff thêm bài viết mới, có thể kèm hình ảnh.\n"
                    + "Yêu cầu: gửi dưới dạng multipart/form-data."
    )
    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> createPost(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String maincontent,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(value = "image", required = false) List<MultipartFile> image) throws IOException {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");

        // Gọi service để tạo bài viết
        Post post = postService.addPostWithImage(title, content,userId,maincontent,categoryId,image);

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
    @PreAuthorize("hasAnyRole('STAFF','ADMIN','ROLE_ROLE_STAFF')")
    @PutMapping(value = "/posts/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "API cập nhật bài viết",
            description = "Cho phép staff cập nhật tiêu đề, nội dung bài viết và thay thế ảnh cũ nếu có."
    )
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("content") String maincontent,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(value = "file", required = false) List<MultipartFile> image) {

        try {
            // Gọi service để cập nhật bài viết
            Post updatedPost = postService.updatePost(id, title, content,maincontent,categoryId,image);

            return ResponseEntity.ok(Collections.singletonMap("message", "Post updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }



    // API Xóa bài viết
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "API xóa bài viết", description =
            "Xóa bài viết dựa trên ID bài viết. Hành động này không thể khôi phục."
    )
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted successfully");
    }

    @Operation(summary = "API tìm kiếm bài viết theo ID, tiêu đề, ID danh mục hoặc tên danh mục",
            description = "Tìm bài viết theo ID, tiêu đề (gần đúng), ID danh mục hoặc tên danh mục.")
    @GetMapping("/posts/search")
    public ResponseEntity<List<PostResponse>> searchPosts(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String categoryName) {

        List<Post> posts = new ArrayList<>();

        if (id != null) {
            // Tìm theo ID bài viết
            Post post = postService.getPostById(id);
            if (post != null) posts.add(post);
        } else if (title != null && !title.isEmpty()) {
            // Tìm theo tiêu đề (gần đúng)
            posts = postService.searchByTitle(title);
        } else if (categoryId != null) {
            // Tìm theo ID danh mục
            posts = postService.searchByCategoryId(categoryId);
        } else if (categoryName != null && !categoryName.isEmpty()) {
            // Tìm theo tên danh mục (gần đúng)
            posts = postService.searchByCategoryName(categoryName);
        } else {
            // Nếu không có điều kiện nào, trả về toàn bộ danh sách
            posts = postService.getAllPosts();
        }

        // Chuyển đổi sang DTO
        List<PostResponse> postResponses = posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(postResponses);
    }


}
