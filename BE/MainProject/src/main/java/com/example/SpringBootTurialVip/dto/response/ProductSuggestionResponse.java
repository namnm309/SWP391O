package com.example.SpringBootTurialVip.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductSuggestionResponse {

    private Long id;
    private String title;
    private String description;
    private String imageName;

    private Double price;
    private Double discountPrice;

    private Boolean isPriority;

    private String origin;           // Nguồn gốc xuất xứ
    private Integer minAgeMonths;    // Độ tuổi tối thiểu (tháng)
    private Integer maxAgeMonths;    // Độ tuổi tối đa (tháng)
    private Integer numberOfDoses;   // Số mũi cần tiêm
}
