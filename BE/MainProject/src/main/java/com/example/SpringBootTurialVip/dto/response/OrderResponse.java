package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {

    @Schema(example = "1", description = "ID của giỏ hàng")
    private Long id;

    @Schema(description = "Thông tin sản phẩm (không chứa thông tin người dùng)")
    private Product product;

    @Schema(example = "2", description = "Số lượng sản phẩm trong giỏ hàng")
    private int quantity;

    @Schema(example = "500000", description = "Tổng giá của sản phẩm trong giỏ hàng")
    private double totalPrice;

    @Schema(example = "1005000", description = "Tổng giá trị đơn hàng bao gồm phí ship")
    private double totalOrderPrice;
}
