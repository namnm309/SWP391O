package com.example.SpringBootTurialVip.shopentity;

import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@Table(name="tbl_orderrequest")
public class OrderRequest {

	private String firstName;

	private String lastName;

	private String email;

	private String mobileNo;
	
	private String paymentType;

}
