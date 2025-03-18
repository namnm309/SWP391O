package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.entity.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    //Lấy danh sách thông báo của khách hàng.
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    //Lấy thông báo chưa đọc
    @Query("SELECT n FROM Notification n WHERE n.readStatus = false AND n.user.id = :userId")
    List<Notification> findUnreadNotifications(@Param("userId") Long userId);


    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);


}

