package com.example.SpringBootTurialVip.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UnderlyingConditionRequestDTO {

    @Schema(description = "Tên bệnh nền", example = "Hen suyễn")
    private String conditionName;

    @Schema(description = "Ghi chú (nếu có)", example = "Được chẩn đoán từ nhỏ")
    private String note;
}
