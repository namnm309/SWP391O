package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.dto.request.PostUpdateRequest;

import com.example.SpringBootTurialVip.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    //Thêm bài viết
    public Post addPostWithImage(String title, String content, Long userId, MultipartFile image) throws IOException;

    // 2. Lấy danh sách tất cả bài viết
    public List<Post> getAllPosts();

    // 3. Lấy bài viết của 1 nhân viên cụ thể
    public List<Post> getPostsByStaff(Long staffId);

    // 4. Cập nhật bài viết
    public Post updatePost(Long id, String title, String content, MultipartFile image) throws IOException;

    public void deletePost(Long id);

    public Post getPostById(Long id);

    public List<Post> searchByTitle(String title);


}
