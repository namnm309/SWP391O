package com.example.SpringBootTurialVip.repository;


import com.example.SpringBootTurialVip.entity.Post;
import com.example.SpringBootTurialVip.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Lấy danh sách bài viết của một staff cụ thể
    List<Post> findByAuthor(User author);

    // Tìm bài viết theo ID
    Optional<Post> findById(Long id);

    // Tìm bài viết theo tiêu đề gần đúng (sử dụng LIKE)
   // List<Post> findByTitleContainingIgnoreCase(String title);


    // Tìm bài viết theo tên gần đúng (dùng LIKE)
    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Post> findByTitleContainingIgnoreCase(@Param("title") String title);




}

