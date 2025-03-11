package com.example.SpringBootTurialVip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpcomingVaccinationResponse {
    private Integer orderDetailId;
    private String vaccineName;
    private LocalDate vaccinationDate;
    private Integer remainingDoses; // Số mũi còn lại
}
