package com.example.VNVC_1.tiemchung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.VNVC_1.tiemchung.model.Feedback;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Feedback, Long> {

    // Tìm feedback theo User ID (chắc chắn userId đúng với field trong entity Feedback)
    List<Feedback> findByUserId(Long userId);
}
