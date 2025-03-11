package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.entity.OrderDetail;
import com.example.SpringBootTurialVip.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor

@Builder
public class ProductOrderResponse {
    private String orderId;
    private LocalDate orderDate;
    //private Product product;
  //  private double price;
    //private int quantity;
    private String status;
    private String paymentType;
   // private OrderDetail orderDetail;
    private Double totalPrice;
    private List<OrderDetailResponse> orderDetails;


    public ProductOrderResponse(String orderId, LocalDate orderDate, String status, String paymentType, Double totalPrice, List<OrderDetailResponse> orderDetails) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.status = status;
        this.paymentType = paymentType;
        this.totalPrice = totalPrice;
        this.orderDetails = orderDetails; // **Gán danh sách OrderDetailResponse vào ProductOrderResponse**
    }

    public ProductOrderResponse(String orderId, LocalDate orderDate, String status, String paymentType, Double totalPrice) {
    }
}
