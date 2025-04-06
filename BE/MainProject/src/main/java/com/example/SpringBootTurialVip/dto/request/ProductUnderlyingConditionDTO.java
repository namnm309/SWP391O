package com.example.SpringBootTurialVip.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUnderlyingConditionDTO {

    @Schema(description = "ID sản phẩm", example = "123")
    private Long productId;

    @Schema(description = "Tên sản phẩm", example = "Vaccine XYZ")
    private String productName;

    @Schema(description = "Tên bệnh nền liên quan", example = "Hen suyễn")
    private String condition;

    @Schema(description = "Mô tả bệnh nền (nếu có)", example = "Ảnh hưởng đến hệ hô hấp, cần lưu ý khi tiêm vaccine")
    private String description;
}
