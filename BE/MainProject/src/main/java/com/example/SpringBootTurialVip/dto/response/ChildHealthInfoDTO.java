package com.example.SpringBootTurialVip.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ChildHealthInfoDTO {

    /* ===== Thông tin bé ===== */
    private Long   childId;
    private String childName;
    private Integer ageMonths;
    private Double height;   // cm
    private Double weight;   // kg

    /* ===== Danh sách bệnh nền của bé ===== */
    private List<UnderlyingConditionResponseDTO> conditions;

    /* ===== Vaccine gợi ý phù hợp ===== */
    private List<ProductSuggestionDTO> vaccines;
}
