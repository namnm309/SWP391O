package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.entity.UnderlyingCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnderlyingConditionRepository extends JpaRepository<UnderlyingCondition, Long> {
    // Phương thức tìm bệnh nền của người dùng (User)
    List<UnderlyingCondition> findByUserId(Long userId);

    //Check trùng condition
    boolean existsByUserIdAndConditionName(Long userId, String conditionName);

}
