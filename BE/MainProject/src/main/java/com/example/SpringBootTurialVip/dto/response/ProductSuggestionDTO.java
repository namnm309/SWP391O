package com.example.SpringBootTurialVip.dto.response;

import lombok.Data;

@Data
public class ProductSuggestionDTO {
    private Long id;
    private String title;
    private String targetGroup;
    private int numberOfDoses;
    private int remainingDoses;
}