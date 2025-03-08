package com.example.SpringBootTurialVip.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReactionRequest {
    private Long childId;
    private String symptoms;
    private Long createdById;

}
