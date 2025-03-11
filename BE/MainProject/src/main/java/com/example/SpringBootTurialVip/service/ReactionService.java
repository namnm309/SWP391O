package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.dto.request.ReactionRequest;
import com.example.SpringBootTurialVip.dto.response.ReactionResponse;
import com.example.SpringBootTurialVip.entity.Reaction;

import java.util.List;

public interface ReactionService {
    Reaction addReactionToOrderDetail(Integer orderDetailId, ReactionRequest request);

    List<ReactionResponse> getReactionsByOrderDetailId(Integer orderDetailId);

    ReactionResponse updateReaction(Long reactionId, ReactionRequest request);

    void deleteReaction(Long reactionId, Long userId);

    List<ReactionResponse> getReactionsByChildId(Long childId);

}
