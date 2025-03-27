package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.entity.UserRelationship;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRelationshipRepository extends JpaRepository<UserRelationship, Long> {
    List<UserRelationship> findByChild(User child);

    // Lấy danh sách trẻ theo relative (người có quan hệ với trẻ)
    List<UserRelationship> findByRelative(User relative);

    // Tìm quan hệ giữa một đứa trẻ và một người thân cụ thể
    Optional<UserRelationship> findByChildAndRelative(User child, User relative);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserRelationship ur WHERE ur.child.id = :userId OR ur.relative.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);


}
