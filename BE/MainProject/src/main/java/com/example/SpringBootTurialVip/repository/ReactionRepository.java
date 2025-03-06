package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    List<Reaction> findByOrderDetailId(Long orderDetailId);
}

