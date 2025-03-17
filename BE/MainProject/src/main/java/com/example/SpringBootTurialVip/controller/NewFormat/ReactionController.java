package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.request.ReactionRequest;
import com.example.SpringBootTurialVip.dto.response.ReactionResponse;
import com.example.SpringBootTurialVip.entity.Reaction;
import com.example.SpringBootTurialVip.service.ReactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reaction")
@RequiredArgsConstructor
@Tag(name="[Reaction]",description = "")
@PreAuthorize("hasAnyRole('STAFF', 'CUSTOMER','ADMIN')")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

//    @Operation(summary = "Thêm phản ứng vào đơn hàng",
//            description = "API này dùng để ghi nhận phản ứng của trẻ em sau khi tiêm vaccine, liên kết với một đơn hàng cụ thể.")
//    @PostMapping("/add/{orderdetailId}")
//    public ResponseEntity<ReactionResponse> addReaction(
//            @Parameter(description = "ID của đơn hàng cần thêm phản ứng", required = true)
//            @PathVariable Long orderdetailId,
//
//            @Parameter(description = "Thông tin chi tiết về phản ứng", required = true)
//            @RequestBody ReactionRequest request) {
//
//        Reaction newReaction = reactionService.addReactionToOrderDetail(Math.toIntExact(orderdetailId), request);
//
//
//
//        return ResponseEntity.ok(new ReactionResponse(newReaction));
//    }

    @Operation(summary = "Thêm phản ứng vào đơn hàng",
            description = "API này dùng để ghi nhận phản ứng của trẻ em sau khi tiêm vaccine, liên kết với một đơn hàng cụ thể.")
    @PostMapping("/add/{orderdetailId}")
    public ResponseEntity<?> addReaction(
            @Parameter(description = "ID của đơn hàng cần thêm phản ứng", required = true)
            @PathVariable Long orderdetailId,

            @Parameter(description = "Thông tin chi tiết về phản ứng", required = true)
            @RequestBody ReactionRequest request) {

        try {
            Reaction newReaction = reactionService.addReactionToOrderDetail(Math.toIntExact(orderdetailId), request);
            return ResponseEntity.ok(new ReactionResponse(newReaction));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(1002, e.getMessage(), null));
        }
    }


    @Operation(summary = "Xem phản ứng sau tiêm theo ID OrderDetail",
            description = "Lấy danh sách phản ứng của một OrderDetail cụ thể.")
    @GetMapping("/order-detail/{orderDetailId}")
    public ResponseEntity<List<ReactionResponse>> getReactionsByOrderDetailId(
            @PathVariable Integer orderDetailId) {

        List<ReactionResponse> reactions = reactionService.getReactionsByOrderDetailId(orderDetailId);

        if (reactions.isEmpty()) {
            return ResponseEntity.noContent().build(); // Trả về HTTP 204 nếu không có phản ứng nào
        }

        return ResponseEntity.ok(reactions);
    }


    @Operation(summary = "Chỉnh sửa phản ứng sau tiêm",
            description = "Cho phép cập nhật nội dung phản ứng của một đơn hàng.")
    @PutMapping("/update/{reactionId}")
    public ResponseEntity<ReactionResponse> updateReaction(@PathVariable Long reactionId,
                                                           @RequestBody ReactionRequest request) {
        ReactionResponse updatedReaction = reactionService.updateReaction(reactionId, request);
        return ResponseEntity.ok(updatedReaction);
    }

    @Operation(summary = "Xóa phản ứng sau tiêm",
            description = "Chỉ cho phép người tạo phản ứng xóa phản ứng đó.")
    @DeleteMapping("/delete/{reactionId}")
    public ResponseEntity<String> deleteReaction(@PathVariable Long reactionId
                                              //   @RequestParam Long userId
                                                    ) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");
        reactionService.deleteReaction(reactionId, userId);
        return ResponseEntity.ok("Reaction deleted successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @Operation(summary = "Lấy danh sách phản ứng dựa theo childId",
            description = "Lấy danh sách tất cả phản ứng tiêm chủng của một trẻ cụ thể dựa trên childId.")
    @GetMapping("/child/{childId}")
    public ResponseEntity<ApiResponse<List<ReactionResponse>>> getReactionsByChildId(
            @PathVariable Long childId) {

        // Lấy danh sách phản ứng theo childId
        List<ReactionResponse> reactions = reactionService.getReactionsByChildId(childId);

        if (reactions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse<>(1001, "No reactions found for childId: " + childId, null));
        }

        return ResponseEntity.ok(new ApiResponse<>(1000, "Reactions retrieved successfully", reactions));
    }

}
