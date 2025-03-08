package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.ReactionRequest;

import com.example.SpringBootTurialVip.dto.response.ReactionResponse;
import com.example.SpringBootTurialVip.entity.OrderDetail;
import com.example.SpringBootTurialVip.entity.ProductOrder;
import com.example.SpringBootTurialVip.entity.Reaction;

import com.example.SpringBootTurialVip.entity.User;
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
    private ReactionRepository reactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Reaction addReactionToProductOrder(Long productOrderId, ReactionRequest request) {
        // 1. Tìm `ProductOrder` theo ID
        ProductOrder productOrder = productOrderRepository.findById(productOrderId)
                .orElseThrow(() -> new RuntimeException("ProductOrder not found with ID: " + productOrderId));

        // 2. Lấy `OrderDetail` từ `ProductOrder`
        OrderDetail orderDetail = productOrder.getOrderDetail();
        if (orderDetail == null) {
            throw new RuntimeException("OrderDetail not found for this ProductOrder");
        }

        //3a.Tự nhập childId
        Long childid=productOrder.getOrderDetail().getChild().getId();

        // 3b. Tìm `child` (trẻ được ghi nhận phản ứng)
        User child = userRepository.findById(childid)
                .orElseThrow(() -> new RuntimeException("Child (User) not found with ID: " + request.getChildId()));

        // Lấy thông tin từ SecurityContextHolder
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userid = jwt.getClaim("id");

        // 4. Tìm `createdBy` (người ghi nhận phản ứng)
        User createdBy = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("User (createdBy) not found with ID: " + userid));



        // 6. Tạo mới `Reaction`
        Reaction reaction = new Reaction();
        reaction.setOrderDetail(orderDetail);
        reaction.setChild(child);
        reaction.setSymptoms(request.getSymptoms());
        reaction.setReportedAt(LocalDateTime.now());
        //reaction.setUpdatedAt(LocalDateTime.now());
        reaction.setCreatedBy(createdBy);


        // 7. Lưu vào database
        return reactionRepository.save(reaction);
    }

    @Override
    public List<ReactionResponse> getReactionsByProductOrderId(Long productOrderId) {
        // Kiểm tra đơn hàng có tồn tại không
        ProductOrder productOrder = productOrderRepository.findById(productOrderId)
                .orElseThrow(() -> new RuntimeException("ProductOrder not found with ID: " + productOrderId));

        // Lấy danh sách phản ứng dựa trên OrderDetail của ProductOrder
        List<Reaction> reactions = reactionRepository.findByOrderDetail(productOrder.getOrderDetail());

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
}

