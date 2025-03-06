package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.ReactionRequest;
import com.example.SpringBootTurialVip.dto.response.ReactionResponse;
import com.example.SpringBootTurialVip.entity.Reaction;
import com.example.SpringBootTurialVip.service.ReactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reaction")
@RequiredArgsConstructor
@Tag(name="[Reaction]",description = "")
public class ReactionController {

    @Autowired
    private ReactionService reactionService;

    @Operation(summary = "Thêm phản ứng vào đơn hàng",
            description = "API này dùng để ghi nhận phản ứng của trẻ em sau khi tiêm vaccine, liên kết với một đơn hàng cụ thể.")
    @PostMapping("/add/{productOrderId}")
    public ResponseEntity<ReactionResponse> addReaction(
            @Parameter(description = "ID của đơn hàng cần thêm phản ứng", required = true)
            @PathVariable Long productOrderId,

            @Parameter(description = "Thông tin chi tiết về phản ứng", required = true)
            @RequestBody ReactionRequest request) {

        Reaction newReaction = reactionService.addReactionToProductOrder(productOrderId, request);



        return ResponseEntity.ok(new ReactionResponse(newReaction));
    }

    @Operation(summary = "Xem phản ứng sau tiêm theo ID đơn hàng",
            description = "Lấy danh sách phản ứng của một đơn hàng cụ thể.")
    @GetMapping("/order/{productOrderId}")
    public ResponseEntity<List<ReactionResponse>> getReactionsByProductOrderId(@PathVariable Long productOrderId) {
        List<ReactionResponse> reactions = reactionService.getReactionsByProductOrderId(productOrderId);
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
    public ResponseEntity<String> deleteReaction(@PathVariable Long reactionId,
                                                 @RequestParam Long userId) {
        reactionService.deleteReaction(reactionId, userId);
        return ResponseEntity.ok("Reaction deleted successfully");
    }
}
