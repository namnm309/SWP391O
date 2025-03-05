package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private Long id;
    private Product product; // Không chứa thông tin user
    private int quantity;
    private double totalPrice;
    private double totalOrderPrice;
}
