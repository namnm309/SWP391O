package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.dto.response.ReactionResponse;
import com.example.SpringBootTurialVip.entity.OrderDetail;
import com.example.SpringBootTurialVip.entity.Reaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    List<Reaction> findByOrderDetailId(Integer orderDetailId);

    //List<ReactionResponse> getReactionsByProductOrderId(Long productOrderId);

    List<Reaction> findByOrderDetail(OrderDetail orderDetail);

    List<Reaction> findByChildId(Long childId);



    @Modifying
    @Transactional
    @Query("DELETE FROM Reaction r WHERE r.createdBy.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Reaction r WHERE r.child.id = :userId")
    void deleteByChildId(@Param("userId") Long userId);



}

