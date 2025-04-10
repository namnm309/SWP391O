package com.example.SpringBootTurialVip.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class GroupedOrderResponse {
    private String orderId;
    private LocalDate orderDate;
    private String status;
    private String paymentType;
    private double totalPrice;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNo;
    private List<ChildVaccinationGroup> orderDetails;
    private Long staffId;
    private String staffName;

}

