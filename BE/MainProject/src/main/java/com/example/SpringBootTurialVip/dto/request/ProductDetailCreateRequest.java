package com.example.SpringBootTurialVip.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailCreateRequest {

    private String sku;  // Mã SKU
    private String batchNumber;  // Số lô
    private String expirationDate;  // Ngày hết hạn (dạng String cho dễ xử lý)
    private Integer quantity;  // Số lượng
    private Integer reservedQuantity;  // Số lượng đã đặt
    private Boolean isActive;  // Trạng thái lô hàng (còn hoạt động hay không)
}