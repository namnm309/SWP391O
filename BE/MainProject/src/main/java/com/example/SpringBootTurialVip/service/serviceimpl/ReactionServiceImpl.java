package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.ReactionRequest;
import com.example.SpringBootTurialVip.entity.ProductOrder;
import com.example.SpringBootTurialVip.entity.Reaction;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.repository.ProductOrderRepository;
import com.example.SpringBootTurialVip.repository.ReactionRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final ReactionRepository reactionRepository;
    private final ProductOrderRepository productOrderRepository;
    private final UserRepository userRepository;

    @Override
    public Reaction recordReaction(ReactionRequest request, String username) {
        // Tìm đơn hàng
        ProductOrder productOrder = productOrderRepository.findById(request.getProductOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Kiểm tra trạng thái đơn hàng có thành công không
        if (!"SUCCESS".equalsIgnoreCase(productOrder.getStatus())) {
            throw new RuntimeException("Reaction can only be recorded for completed orders.");
        }

        // Lấy thông tin trẻ em và người tạo
        User child = userRepository.findById(request.getChildId())
                .orElseThrow(() -> new RuntimeException("Child not found"));

        User createdBy = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tạo phản ứng mới
        Reaction reaction = new Reaction();
        reaction.setOrderDetail(productOrder.getOrderDetail());
        reaction.setChild(child);
        reaction.setSymptoms(request.getSymptoms());
        reaction.setCreatedBy(createdBy);

        return reactionRepository.save(reaction);
    }

    @Override
    public List<Reaction> getReactionsByProductOrderId(Long productOrderId) {
        ProductOrder order = productOrderRepository.findById(productOrderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return reactionRepository.findByOrderDetailId(Long.valueOf(order.getOrderDetail().getId()));
    }

    @Override
    public Reaction updateReaction(Long reactionId, String symptoms, String username) {
        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new RuntimeException("Reaction not found"));

        User updatedBy = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        reaction.setSymptoms(symptoms);
        reaction.setUpdatedBy(updatedBy);
        reaction.setUpdatedAt(LocalDateTime.now());

        return reactionRepository.save(reaction);
    }
}

