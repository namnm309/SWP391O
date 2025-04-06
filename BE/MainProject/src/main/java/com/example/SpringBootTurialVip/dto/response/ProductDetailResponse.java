package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.entity.ProductDetails;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailResponse {
    private Long id;
    private String sku;
    private String batchNumber;
    private String expirationDate;
    private Integer quantity;
    private Integer reservedQuantity;
    private Boolean isActive;

    public ProductDetailResponse(ProductDetails productDetails) {
        this.id = productDetails.getId();
        this.sku = productDetails.getSku();
        this.batchNumber = productDetails.getBatchNumber();
        this.expirationDate = productDetails.getExpirationDate().toString();
        this.quantity = productDetails.getQuantity();
        this.reservedQuantity = productDetails.getReservedQuantity();
        this.isActive = productDetails.getIsActive();
    }

    // Getters and setters
}
