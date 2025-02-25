package com.example.SpringBootTurialVip.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCartQuantityRequest {

    @Schema(example = "increase", description = "Loại thay đổi số lượng ('increase' hoặc 'decrease')")
    @NotBlank(message = "Quantity change type is required")
    private String sy;

    @Schema(example = "123", description = "ID của giỏ hàng cần cập nhật")
    @NotNull(message = "Cart ID is required")
    private Long cid;
}
