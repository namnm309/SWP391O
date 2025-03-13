package com.example.SpringBootTurialVip.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrderDetailResponse {
    private Integer id;
    private String productName;
    private int quantity;
    private String orderId; // Liên kết với ProductOrder
    private LocalDate vaccinationDate;
    private double price;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNo;

    public OrderDetailResponse(String productName, int quantity, String orderId, LocalDate vaccinationDate, double price,
                               String firstName, String lastName, String email, String mobileNo) {
        this.productName = productName;
        this.quantity = quantity;
        this.orderId = orderId;
        this.vaccinationDate = vaccinationDate;
        this.price = price;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNo = mobileNo;
    }


    public OrderDetailResponse(Integer id,
                               String title,
                               Integer quantity,
                               LocalDate vaccinationDate,
                               Double discountPrice,
                               String firstName,
                               String lastName,
                               String email,
                               String mobileNo) {
        this.id = id;
        this.productName = title;
        this.quantity = quantity;
        this.vaccinationDate = vaccinationDate;
        this.price = discountPrice;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNo = mobileNo;
    }



    public OrderDetailResponse(Integer id,
                               String title,
                               Integer quantity,
                               String orderId,
                               LocalDate vaccinationDate,
                               Double discountPrice,
                               String firstName,
                               String lastName,
                               String email,
                               String mobileNo) {
        this.id = id;
        this.productName = title;
        this.quantity = quantity;
        this.orderId=orderId;
        this.vaccinationDate = vaccinationDate;
        this.price = discountPrice;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNo = mobileNo;
    }
}

