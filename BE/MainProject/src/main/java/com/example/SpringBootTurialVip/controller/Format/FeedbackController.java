package com.example.SpringBootTurialVip.controller.Format;

import com.example.SpringBootTurialVip.dto.request.FeedbackRequest;
import com.example.SpringBootTurialVip.entity.Feedback;
import com.example.SpringBootTurialVip.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@Tag(name="[Feedback]",description = "")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    //APi gửi đánh giá
    @Operation(
            summary = "API gửi đánh giá(customer)",
            description = "Cho phép khách hàng gửi đánh giá về dịch vụ tiêm chủng."
    )
    @PostMapping(value = "/feedback", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Feedback> submitFeedback(
            @RequestBody FeedbackRequest feedbackRequest) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");
        return ResponseEntity.ok(feedbackService.submitFeedback(userId, feedbackRequest.getRating(), feedbackRequest.getComment()));
    }

    //API xem đánh giá
    @Operation(
            summary = "API xem đánh giá của người dùng(customer)",
            description = "Trả về danh sách đánh giá của khách hàng hiện tại."
    )
    @GetMapping("/feedback")
    public ResponseEntity<List<Feedback>> getMyFeedback() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");
        return ResponseEntity.ok(feedbackService.getFeedbackByUser(userId));
    }

    //API update đánh giá
    @Operation(
            summary = "API cập nhật đánh giá(customer)",
            description = "Cho phép khách hàng chỉnh sửa đánh giá đã gửi. ID được tự động xác định."
    )
    @PutMapping(value = "/feedback", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Feedback> updateFeedback(
            @RequestBody FeedbackRequest feedbackRequest) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");
        return ResponseEntity.ok(feedbackService.submitOrUpdateFeedback(userId, feedbackRequest.getRating(), feedbackRequest.getComment()));
    }

    @Operation(
            summary = "API xóa đánh giá(customer)",
            description = "Cho phép khách hàng xóa đánh giá của mình. ID được tự động xác định."
    )
    @DeleteMapping("/feedback")
    public ResponseEntity<String> deleteFeedback() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");
        feedbackService.deleteFeedbackByUser(userId);
        return ResponseEntity.ok("Feedback deleted successfully");
    }

    //API xem đánh giá chưa phản hồi
    @Operation(
            summary = "API lấy danh sách đánh giá chưa được phản hồi(staff)",
            description = "Trả về danh sách tất cả đánh giá của khách hàng chưa được phản hồi."
    )
    @GetMapping("/feedback/unreplied")
    public ResponseEntity<List<Feedback>> getUnrepliedFeedbacks() {
        return ResponseEntity.ok(feedbackService.getUnrepliedFeedbacks());
    }

    //API reply
    @Operation(
            summary = "API phản hồi đánh giá của khách hàng(staff)",
            description = "Cho phép nhân viên phản hồi đánh giá của khách hàng.\n"
                    + "Sau khi phản hồi, đánh giá sẽ tự động được đánh dấu là đã phản hồi."
    )
    @PutMapping("/feedback/{id}/reply")
    public ResponseEntity<?> replyFeedback(
            @PathVariable("id") Long id,
            @RequestParam("reply") String reply) {
        try {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = jwt.getClaim("id");
            return ResponseEntity.ok(feedbackService.replyFeedback(id, reply, userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @Operation(
            summary = "API lấy danh sách đánh giá theo số sao(admin)",
            description = "Cho phép admin lọc và xem danh sách đánh giá theo số sao từ 1 đến 5."
    )
    @GetMapping("/feedback/rating/{stars}")
    public ResponseEntity<List<Feedback>> getFeedbackByRating(@PathVariable int stars) {
        return ResponseEntity.ok(feedbackService.getFeedbackByRating(stars));
    }

    @Operation(
            summary = "API lấy số sao trung bình(admin)",
            description = "Cho phép admin xem số sao trung bình của tất cả đánh giá trên hệ thống."
    )
    @GetMapping("/feedback/average-rating")
    public ResponseEntity<Double> getAverageRating() {
        return ResponseEntity.ok(feedbackService.getAverageRating());
    }

}
