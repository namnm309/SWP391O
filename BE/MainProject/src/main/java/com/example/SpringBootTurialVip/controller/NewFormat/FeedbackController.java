package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@Tag(name="[Feedback]",description = "")
@PreAuthorize("hasAnyRole('CUSTOMER','STAFF', 'ADMIN')")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    //APi gửi đánh giá
    @PreAuthorize("hasAnyRole('CUSTOMER','TEST')")
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
    @PreAuthorize("hasAnyRole('CUSTOMER','STAFF','ADMIN','TEST')")
    @Operation(
            summary = "API xem đánh giá của người dùng dựa trên token (customer)",
            description = "Trả về danh sách đánh giá của khách hàng hiện tại."
    )
    @GetMapping("/feedback")
    public ResponseEntity<List<Feedback>> getMyFeedback() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");
        return ResponseEntity.ok(feedbackService.getFeedbackByUser(userId));
    }

    //API xem tất cả đánh giá


    //Tìm đánh giá của người dủng = id của người đó

    //API update đánh giá

    @PreAuthorize("hasAnyRole('CUSTOMER','STAFF','ADMIN','TEST')")
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

    //API xóa đánh giá
    @PreAuthorize("hasAnyRole('CUSTOMER','STAFF')")
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
    @PreAuthorize("hasAnyRole('TEST','STAFF', 'ADMIN')")
    @Operation(
            summary = "API lấy danh sách đánh giá chưa được phản hồi(staff,admin)",
            description = "Trả về danh sách tất cả đánh giá của khách hàng chưa được phản hồi."
    )
    @GetMapping("/feedback/unreplied")
    public ResponseEntity<List<Feedback>> getUnrepliedFeedbacks() {
        return ResponseEntity.ok(feedbackService.getUnrepliedFeedbacks());
    }

    //API reply
    @PreAuthorize("hasAnyRole('STAFF','TEST','ADMIN')")
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

    //API xem đánh giá theo số sao
    @Operation(
            summary = "API xem danh sách đánh giá theo số sao(all)",
            description = "Cho phép admin lọc và xem danh sách đánh giá theo số sao từ 1 đến 5."
    )
    @GetMapping("/feedback/rating/{stars}")
    public ResponseEntity<List<Feedback>> getFeedbackByRating(@PathVariable int stars) {
        return ResponseEntity.ok(feedbackService.getFeedbackByRating(stars));
    }

    //API xem số sao trung bình
    @PreAuthorize("hasAnyRole('ADMIN','TEST')")
    @Operation(
            summary = "API lấy số sao trung bình(admin)",
            description = "Cho phép admin xem số sao trung bình của tất cả đánh giá trên hệ thống."
    )
    @GetMapping("/feedback/average-rating")
    public ResponseEntity<Double> getAverageRating() {
        return ResponseEntity.ok(feedbackService.getAverageRating());
    }

    //API Xem đánh giá từ cao đến thấp
    @Operation(summary = "Lấy danh sách đánh giá từ 5 sao đến 1 sao(all)")
    @GetMapping("/sorted/desc")
    public ResponseEntity<ApiResponse<List<Feedback>>> getFeedbacksDesc() {
        List<Feedback> feedbacks = feedbackService.getFeedbacksSortedByRatingDesc();
        return ResponseEntity.ok(new ApiResponse<>(0, "Lấy đánh giá từ 5 sao đến 1 sao thành công", feedbacks));
    }

    //API xem đánh giá từ thấp đến cao
    @Operation(summary = "Lấy danh sách đánh giá từ 1 sao đến 5 sao(all)")
    @GetMapping("/sorted/asc")
    public ResponseEntity<ApiResponse<List<Feedback>>> getFeedbacksAsc() {
        List<Feedback> feedbacks = feedbackService.getFeedbacksSortedByRatingAsc();
        return ResponseEntity.ok(new ApiResponse<>(0, "Lấy đánh giá từ 1 sao đến 5 sao thành công", feedbacks));
    }

    //API xem toàn bộ feedback
    // API xem toàn bộ feedback (ai cũng xem được)

    @Operation(
            summary = "API lấy danh sách toàn bộ đánh giá (public)",
            description = "Trả về danh sách tất cả các đánh giá trên hệ thống, ai cũng có thể xem."
    )
    @GetMapping("/feedback/all")
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }

}
