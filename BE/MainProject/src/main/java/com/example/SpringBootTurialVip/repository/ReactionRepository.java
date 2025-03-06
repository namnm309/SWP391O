package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.dto.response.ReactionResponse;
import com.example.SpringBootTurialVip.entity.OrderDetail;
import com.example.SpringBootTurialVip.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    List<Reaction> findByOrderDetailId(Long orderDetailId);

    //List<ReactionResponse> getReactionsByProductOrderId(Long productOrderId);

    List<Reaction> findByOrderDetail(OrderDetail orderDetail);

}

