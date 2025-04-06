package com.example.SpringBootTurialVip.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailRequest {
    private String sku;
    private String batchNumber;
    private String expirationDate;  // Định dạng là String, ví dụ "2025-04-05"
    private Integer quantity;


}
