package com.example.SpringBootTurialVip.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReactionRequest {
    //private Long childId;
    private String symptoms;
   // private Long createdById;

    private boolean badInjection; // Có phản ứng nặng hay không

    // Staff cập nhật xử lý
    private String handlingNote;


}
