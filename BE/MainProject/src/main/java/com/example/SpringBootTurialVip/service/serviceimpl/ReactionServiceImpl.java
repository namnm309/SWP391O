package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.ReactionRequest;
import com.example.SpringBootTurialVip.dto.response.ReactionResponse;
import com.example.SpringBootTurialVip.entity.OrderDetail;
import com.example.SpringBootTurialVip.entity.ProductOrder;
import com.example.SpringBootTurialVip.entity.Reaction;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.repository.OrderDetailRepository;
import com.example.SpringBootTurialVip.repository.ProductOrderRepository;
import com.example.SpringBootTurialVip.repository.ReactionRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.ReactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Reaction addReactionToOrderDetail(Integer orderDetailId, ReactionRequest request) {
        // 1. Tìm `OrderDetail` theo ID
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new RuntimeException("OrderDetail not found with ID: " + orderDetailId));


        // 2. Lấy ProductOrder từ orderId của OrderDetail
        ProductOrder productOrder = productOrderRepository.findByOrderId(orderDetail.getOrderId());
        if (productOrder == null) {
            throw new RuntimeException("ProductOrder not found for OrderDetail ID: " + orderDetailId);
        }

        // 3. Kiểm tra trạng thái thanh toán của ProductOrder
        if (!"PAID".equalsIgnoreCase(productOrder.getStatus())) {
            throw new RuntimeException("Cannot add reaction. The order has not been paid yet.");
        }

        // 4. Tìm `child` (trẻ được ghi nhận phản ứng)
        User child = orderDetail.getChild();

        if (child == null) {
            throw new RuntimeException("Child not found for this OrderDetail");
        }

        // 5. Lấy thông tin người tạo phản ứng (staff/admin)
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");

        User createdBy = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User (createdBy) not found with ID: " + userId));

        // 6. Tạo mới `Reaction`
        Reaction reaction = new Reaction();
        reaction.setOrderDetail(orderDetail);
        reaction.setChild(child);
        reaction.setSymptoms(request.getSymptoms());
        reaction.setReportedAt(LocalDateTime.now());
        reaction.setCreatedBy(createdBy);

        // 7. Lưu vào database
        return reactionRepository.save(reaction);
    }

    @Override
    public List<ReactionResponse> getReactionsByOrderDetailId(Integer orderDetailId) {
        // Kiểm tra `OrderDetail` có tồn tại không
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new RuntimeException("OrderDetail not found with ID: " + orderDetailId));

        // Lấy danh sách phản ứng dựa trên OrderDetail
        List<Reaction> reactions = reactionRepository.findByOrderDetail(orderDetail);

        // Chuyển đổi danh sách sang Response DTO
        return reactions.stream().map(ReactionResponse::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReactionResponse updateReaction(Long reactionId, ReactionRequest request) {
        // Lấy phản ứng từ DB
        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new RuntimeException("Reaction not found with ID: " + reactionId));

        // Cập nhật thông tin phản ứng
        reaction.setSymptoms(request.getSymptoms());
        reaction.setReportedAt(LocalDateTime.now());

        return new ReactionResponse(reactionRepository.save(reaction));
    }

    @Override
    @Transactional
    public void deleteReaction(Long reactionId, Long userId) {
        // Lấy phản ứng từ DB
        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new RuntimeException("Reaction not found with ID: " + reactionId));

        // Kiểm tra người dùng có phải người tạo phản ứng không
        if (!reaction.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("Only the creator of the reaction can delete it.");
        }

        reactionRepository.delete(reaction);
    }

    @Override
    public List<ReactionResponse> getReactionsByChildId(Long childId) {
        List<Reaction> reactions = reactionRepository.findByChildId(childId);

        return reactions.stream()
                .map(ReactionResponse::new)
                .collect(Collectors.toList());
    }

}
