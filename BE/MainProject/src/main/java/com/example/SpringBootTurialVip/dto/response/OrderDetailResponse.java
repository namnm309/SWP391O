package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.enums.OrderDetailStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDetailResponse {
    private Integer orderdetialid;
    private String productName;
    private int quantity;
    private String orderId; // Liên kết với ProductOrder
    private LocalDateTime vaccinationDate;
    private double price;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNo;
    private String orderDetailStatus;
    private String childName;
    private Long childId;
    private Long staffId;
    private String staffName;

    public OrderDetailResponse(String productName,
                               int quantity,
                               String orderId,
                               LocalDateTime vaccinationDate,
                               double price,
                               String firstName,
                               String lastName,
                               String email,
                               String mobileNo) {
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


    public OrderDetailResponse(Integer orderdetialid,
                               String title,
                               Integer quantity,
                               LocalDateTime vaccinationDate,
                               Double discountPrice,
                               String firstName,
                               String lastName,
                               String email,
                               String mobileNo) {
        this.orderdetialid = orderdetialid;
        this.productName = title;
        this.quantity = quantity;
        this.vaccinationDate = vaccinationDate;
        this.price = discountPrice;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNo = mobileNo;
    }



    public OrderDetailResponse(Integer orderdetialid,
                               String title,
                               Integer quantity,
                               String orderId,
                               LocalDateTime vaccinationDate,
                               Double discountPrice,
                               String firstName,
                               String lastName,
                               String email,
                               String mobileNo) {
        this.orderdetialid = orderdetialid;
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

    public OrderDetailResponse(Integer orderdetialid,
                               String title,
                               Integer quantity,
                               String orderId,
                               LocalDateTime vaccinationDate,
                               Double discountPrice,
                               String firstName,
                               String lastName,
                               String email,
                               String mobileNo,
                               String orderDetailStatus,
                               Long staffId,
                               String staffName) {
        this.orderdetialid = orderdetialid;
        this.productName = title;
        this.quantity = quantity;
        this.orderId=orderId;
        this.vaccinationDate = vaccinationDate;
        this.price = discountPrice;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNo = mobileNo;
        this.orderDetailStatus=orderDetailStatus;
        this.staffId=staffId;
        this.staffName=staffName;
    }


    public OrderDetailResponse(Integer orderdetialid,
                               String title,
                               Integer quantity,
                               String orderId,
                               LocalDateTime vaccinationDate,
                               Double discountPrice,
                               String firstName,
                               String lastName,
                               String email,
                               String mobileNo,
                               User child) {
        this.orderdetialid = orderdetialid;
        this.productName = title;
        this.quantity = quantity;
        this.orderId=orderId;
        this.vaccinationDate = vaccinationDate;
        this.price = discountPrice;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNo = mobileNo;
        this.childName=child.getFullname();
        this.childId=child.getId();
    }

    public OrderDetailResponse(Integer id,
                               String title,
                               Integer quantity,
                               String orderId,
                               LocalDateTime vaccinationDate,
                               Double discountPrice,
                               String firstName,
                               String lastName,
                               String email,
                               String mobileNo,
                               String name,
                               String fullname,
                               Long id1) {
        this.orderdetialid = id;
        this.productName = title;
        this.quantity = quantity;
        this.orderId=orderId;
        this.vaccinationDate = vaccinationDate;
        this.price = discountPrice;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNo = mobileNo;
        this.orderDetailStatus=name;
        this.childName=fullname;
        this.childId=id1;
    }

    public OrderDetailResponse(Integer id,
                               String title,
                               Integer quantity,
                               String orderId,
                               LocalDateTime vaccinationDate,
                               Double discountPrice,
                               String firstName,
                               String lastName,
                               String email,
                               String mobileNo,
                               String name,
                               String fullname,
                               Long id1,
                               Long staffId,
                               String staffName) {
        this.orderdetialid = id;
        this.productName = title;
        this.quantity = quantity;
        this.orderId=orderId;
        this.vaccinationDate = vaccinationDate;
        this.price = discountPrice;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNo = mobileNo;
        this.orderDetailStatus=name;
        this.childName=fullname;
        this.childId=id1;
        this.staffId=staffId;
        this.staffName=staffName;
    }
}

