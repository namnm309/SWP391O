package com.example.SpringBootTurialVip.controller.NewFormat;


import com.example.SpringBootTurialVip.dto.request.ReactionRequest;
import com.example.SpringBootTurialVip.entity.Reaction;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.ReactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
@Slf4j
public class ReactionController {
    private final ReactionService reactionService;

    @Autowired
    private UserRepository userRepository;

    // API để khách hàng hoặc nhân viên ghi nhận phản ứng sau tiêm
    @Operation(
            summary = "API ghi nhận phản ứng sau tiêm",
            description = "Cho phép khách hàng hoặc nhân viên ghi nhận phản ứng sau tiêm."
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reaction> recordReaction(
            @RequestBody ReactionRequest request) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReactionRequest userId = jwt.getClaim("id");
        return ResponseEntity.ok(reactionService.recordReaction(userId, String.valueOf(request)));
    }

    @Operation(summary = "V2Ghi nhận phản ứng sau tiêm", description = "Khách hàng hoặc nhân viên ghi nhận phản ứng sau tiêm cho trẻ.")
    @PostMapping
    public ResponseEntity<Reaction> recordReactionV2(
            @RequestBody @Parameter(description = "Dữ liệu phản ứng sau tiêm") ReactionRequest request)
            //@AuthenticationPrincipal UserDetails userDetails)
            {
                Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Long userId = jwt.getClaim("id");
                
                User nameuser= userRepository.findById(userId);
        Reaction reaction = reactionService.recordReaction(request,nameuser);
        return ResponseEntity.ok(reaction);
    }



    // API để lấy danh sách phản ứng của một đơn hàng
    @GetMapping("/{productOrderId}")
    public ResponseEntity<List<Reaction>> getReactions(@PathVariable Long productOrderId) {
        return ResponseEntity.ok(reactionService.getReactionsByProductOrderId(productOrderId));
    }

    // API để cập nhật phản ứng
    @PutMapping("/{reactionId}")
    public ResponseEntity<Reaction> updateReaction(@PathVariable Long reactionId,
                                                   @RequestParam String symptoms,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        Reaction updatedReaction = reactionService.updateReaction(reactionId, symptoms, userDetails.getUsername());
        return ResponseEntity.ok(updatedReaction);
    }
}
