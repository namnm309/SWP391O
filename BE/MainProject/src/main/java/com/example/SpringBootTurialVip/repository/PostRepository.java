package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.entity.Post;
import com.example.SpringBootTurialVip.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Lấy danh sách bài viết của một staff cụ thể
    List<Post> findByAuthor(User author);
}

