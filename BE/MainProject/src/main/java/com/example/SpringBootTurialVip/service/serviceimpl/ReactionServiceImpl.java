package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.ReactionHandlingRequest;
import com.example.SpringBootTurialVip.dto.request.ReactionRequest;
import com.example.SpringBootTurialVip.dto.response.ReactionResponse;
import com.example.SpringBootTurialVip.entity.*;
import com.example.SpringBootTurialVip.enums.OrderDetailStatus;
import com.example.SpringBootTurialVip.repository.*;
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

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    @Transactional
    public Reaction addReactionToOrderDetail(Integer orderDetailId, ReactionRequest request) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy OrderDetail với ID: " + orderDetailId));

        if (orderDetail.getStatus() != OrderDetailStatus.DA_TIEM) {
            throw new RuntimeException("Vaccine chưa được tiêm nên không thể ghi phản ứng sau tiêm !");
        }

        LocalDateTime vaccinationDate = orderDetail.getVaccinationDate();
        if (vaccinationDate != null && LocalDateTime.now().isAfter(vaccinationDate.plusHours(24))) {
            throw new RuntimeException("Phản ứng sau tiêm chỉ được ghi nhận trong vòng 24 giờ kể từ ngày tiêm.");
        }

        // Chặn ghi nhận phản ứng nếu đã tồn tại
        boolean exists = reactionRepository.existsByOrderDetail(orderDetail);
        if (exists) {
            throw new RuntimeException("Đơn hàng này đã được ghi nhận phản ứng, không thể ghi lại.");
        }

        // Tạo phản ứng mới
        ProductOrder productOrder = productOrderRepository.findByOrderId(orderDetail.getOrderId());
        if (productOrder == null) throw new RuntimeException("Không tìm thấy đơn hàng.");

        User child = orderDetail.getChild();
        if (child == null) throw new RuntimeException("Không tìm thấy trẻ.");

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");
        User createdBy = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng tạo phản ứng."));

        Reaction reaction = new Reaction();
        reaction.setOrderDetail(orderDetail);
        reaction.setChild(child);
        reaction.setSymptoms(request.getSymptoms());
        reaction.setReportedAt(LocalDateTime.now());
        reaction.setCreatedBy(createdBy);
        reaction.setUpdatedAt(LocalDateTime.now());
        reaction.setBadInjection(request.isBadInjection());

        Reaction savedReaction = reactionRepository.save(reaction);

        // Nếu có phản ứng nặng → gửi thông báo đến STAFF
        if (request.isBadInjection()) {
            List<User> staffList = userRepository.findAll().stream()
                    .filter(user -> user.getRoles().stream()
                            .anyMatch(role -> role.getName().equals("STAFF")))
                    .collect(Collectors.toList());

            for (User staff : staffList) {
                Notification notification = new Notification();
                notification.setUser(staff);
                notification.setSender(createdBy);
                notification.setCreatedAt(LocalDateTime.now());
                notification.setReadStatus(false);
                notification.setMessage("Phản ứng nghiêm trọng sau tiêm từ trẻ: " + child.getFullname()
                        + " | Mã OrderDetail: " + orderDetail.getId()
                        + " | Triệu chứng: " + request.getSymptoms());

                notificationRepository.save(notification);
            }
        }

        return savedReaction;
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

    @Override
    @Transactional
    public void handleReaction(Long reactionId, ReactionHandlingRequest request, Long staffId) {
        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new RuntimeException("Reaction not found with ID: " + reactionId));

        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with ID: " + staffId));

        reaction.setHandlingNote(request.getHandlingNote());
        reaction.setHandledBy(staff);
        reaction.setHandledAt(LocalDateTime.now());

        reactionRepository.save(reaction);
    }


}
