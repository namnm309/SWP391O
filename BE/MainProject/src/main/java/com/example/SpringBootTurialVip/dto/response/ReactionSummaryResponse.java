package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.entity.Reaction;
import lombok.Data;

@Data
public class ReactionSummaryResponse {

    private Long id;
    private Long orderDetailId;
    private String symptoms;
    private String handlingNote;  // Phản hồi từ staff

    // Constructor chỉ lấy các field cần thiết
    public ReactionSummaryResponse(Long id, Long orderDetailId, String symptoms, String handlingNote) {
        this.id = id;
        this.orderDetailId = orderDetailId;
        this.symptoms = symptoms;
        this.handlingNote = handlingNote;
    }

    // Constructor tạo từ đối tượng Reaction
    public ReactionSummaryResponse(Reaction reaction) {
        this.id = reaction.getId();
        this.orderDetailId = Long.valueOf(reaction.getOrderDetail().getId());
        this.symptoms = reaction.getSymptoms();
        this.handlingNote = reaction.getHandlingNote();  // Chỉ lấy handlingNote
    }
}
