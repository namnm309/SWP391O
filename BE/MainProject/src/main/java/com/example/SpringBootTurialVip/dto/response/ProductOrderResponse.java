package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.shopentity.OrderDetail;
import com.example.SpringBootTurialVip.shopentity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOrderResponse {
    private String orderId;
    private LocalDate orderDate;
    private Product product;
    private double price;
    private int quantity;
    private String status;
    private String paymentType;
    private OrderDetail orderDetail;
}
