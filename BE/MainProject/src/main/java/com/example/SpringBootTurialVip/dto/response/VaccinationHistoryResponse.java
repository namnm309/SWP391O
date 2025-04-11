package com.example.SpringBootTurialVip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class VaccinationHistoryResponse {
    private Integer orderDetailId;
    private String vaccineName;
    private LocalDateTime vaccinationDate;
    private Integer quantity;
    private ReactionResponse reaction;

    public VaccinationHistoryResponse(Integer orderDetailId,
                                      String vaccineName,
                                      LocalDateTime vaccinationDate,
                                      Integer quantity
                                      ) {
        this.orderDetailId = orderDetailId;
        this.vaccineName = vaccineName;
        this.vaccinationDate = vaccinationDate;
        this.quantity = quantity;
        this.reaction = reaction;  // Phản ứng sau tiêm

    }

    public VaccinationHistoryResponse(Integer orderDetailId,
                                      String vaccineName,
                                      LocalDateTime vaccinationDate,
                                      Integer quantity,
                                      ReactionResponse reaction) {
        this.orderDetailId = orderDetailId;
        this.vaccineName = vaccineName;
        this.vaccinationDate = vaccinationDate;
        this.quantity = quantity;
        this.reaction = reaction;  // Phản ứng sau tiêm
    }
}
