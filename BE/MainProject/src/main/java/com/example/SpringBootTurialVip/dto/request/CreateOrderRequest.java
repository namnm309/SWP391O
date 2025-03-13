package com.example.SpringBootTurialVip.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    @NotEmpty(message = "ProductId list cannot be empty")
    @Size(min = 1, message = "At least one product is required")
    private List<Long> productId;

    @NotEmpty(message = "Quantity list cannot be empty")
    @Size(min = 1, message = "At least one quantity is required")
    private List<@Min(value = 1, message = "Quantity must be greater than 0") Integer> quantity;

    private OrderRequest orderRequest;

    public boolean isValidSize() {
        return productId != null && quantity != null && productId.size() == quantity.size();
    }
}
