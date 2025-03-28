package com.example.SpringBootTurialVip.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VaccineItem {
    private Long id;
    private Long productId;
    private String name;
    private double price;
    private String status;
    private LocalDateTime date;
}
