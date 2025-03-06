package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.entity.Reaction;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReactionResponse {
    private Long id;
    private String symptoms;
    private LocalDateTime reportedAt;
    private Long childId;
    private Long createdById;

    public ReactionResponse(Reaction reaction) {
        if (reaction != null) {
            this.id = reaction.getId();
            this.symptoms = reaction.getSymptoms();
            this.reportedAt = reaction.getReportedAt();
            this.childId = reaction.getChild() != null ? reaction.getChild().getId() : null;
            this.createdById = reaction.getCreatedBy() != null ? reaction.getCreatedBy().getId() : null;
        }
    }
}
