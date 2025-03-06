package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.dto.request.ReactionRequest;
import com.example.SpringBootTurialVip.entity.Reaction;

import java.util.List;

public interface ReactionService {
    Reaction recordReaction(ReactionRequest request, String username);

    List<Reaction> getReactionsByProductOrderId(Long productOrderId);

    Reaction updateReaction(Long reactionId, String symptoms, String username);
}
