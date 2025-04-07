package com.example.SpringBootTurialVip.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SKUResponse {

    private Long id;
    private String name;
    private String sku;
    private String batch;            // Mã lô (batch)
    private LocalDate expiryDate;    // Ngày hết hạn
    private Long productId;          // ID sản phẩm

    // Constructor
    public SKUResponse(Long id, String name, String sku, String batch, LocalDate expiryDate, Long productId) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.batch = batch;
        this.expiryDate = expiryDate;
        this.productId = productId;
    }


}
