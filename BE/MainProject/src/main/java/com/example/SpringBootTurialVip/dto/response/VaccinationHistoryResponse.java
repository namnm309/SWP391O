package com.example.SpringBootTurialVip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationHistoryResponse {
    private Integer orderDetailId;
    private String vaccineName;
    private LocalDateTime vaccinationDate;
    private Integer quantity;
}
