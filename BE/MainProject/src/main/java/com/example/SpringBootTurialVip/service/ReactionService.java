package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.dto.request.ReactionRequest;
import com.example.SpringBootTurialVip.dto.response.ReactionResponse;
import com.example.SpringBootTurialVip.entity.Reaction;

import java.util.List;

public interface ReactionService {
    Reaction addReactionToProductOrder(Long productOrderId, ReactionRequest request);

    public List<ReactionResponse> getReactionsByProductOrderId(Long productOrderId);

    public ReactionResponse updateReaction(Long reactionId, ReactionRequest request);

    public void deleteReaction(Long reactionId, Long userId);
}
