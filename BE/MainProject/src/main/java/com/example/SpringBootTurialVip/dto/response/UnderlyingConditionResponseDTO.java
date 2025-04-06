package com.example.SpringBootTurialVip.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UnderlyingConditionResponseDTO {

    @Schema(description = "ID bệnh nền", example = "1")
    private Long id;

    @Schema(description = "Tên bệnh nền", example = "Hen suyễn")
    private String conditionName;

    @Schema(description = "Ghi chú", example = "Được chẩn đoán từ nhỏ")
    private String note;
}
