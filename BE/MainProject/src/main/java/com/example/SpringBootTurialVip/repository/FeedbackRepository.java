package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.entity.Feedback;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // Lấy feedback chưa được phản hồi
    List<Feedback> findByRepliedFalse();

    // Lấy feedback theo rating từ 1 đến 5 sao
    List<Feedback> findByRating(int rating);

    // Tính số sao trung bình
    @Query("SELECT AVG(f.rating) FROM Feedback f")
    Double findAverageRating();

    @Modifying
    @Transactional
    @Query("DELETE FROM Feedback f WHERE f.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    Optional<Feedback> findByUserId(Long userId);

    // Lấy danh sách đánh giá theo rating giảm dần (5 -> 1)
    List<Feedback> findAllByOrderByRatingDesc();

    // Lấy danh sách đánh giá theo rating tăng dần (1 -> 5)
    List<Feedback> findAllByOrderByRatingAsc();
}

