package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.entity.Reaction;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReactionResponse {

    private Long id;
    private String symptoms;
    private Long orderDetailId;
    private Long childId;
    private String childName;
    private LocalDateTime reportedAt;
    private LocalDateTime updatedAt;
    private Long createdById;
    private String createdByName;
    // --- Phản hồi từ staff ---
    private String handlingNote;
    private Long handledById;
    private String handledByName;
    private LocalDateTime handledAt;

    public ReactionResponse(Reaction reaction) {
        this.id = reaction.getId();
        this.symptoms = reaction.getSymptoms();

        this.orderDetailId = Long.valueOf(reaction.getOrderDetail().getId());
        this.childId = reaction.getChild().getId();
        this.childName = reaction.getChild().getFullname();

        this.reportedAt = reaction.getReportedAt();
        this.updatedAt = reaction.getUpdatedAt();

        this.createdById = reaction.getCreatedBy().getId();
        this.createdByName = reaction.getCreatedBy().getFullname();

        if (reaction.getHandledBy() != null) {
            this.handlingNote = reaction.getHandlingNote();
            this.handledById = reaction.getHandledBy().getId();
            this.handledByName = reaction.getHandledBy().getFullname();
            this.handledAt = reaction.getHandledAt();
        }
    }
}

