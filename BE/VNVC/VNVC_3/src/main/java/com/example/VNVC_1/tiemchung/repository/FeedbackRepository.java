package com.example.VNVC_1.tiemchung.repository;

import com.example.VNVC_1.tiemchung.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Optional<Feedback> findByUserId(Long userId);
}
