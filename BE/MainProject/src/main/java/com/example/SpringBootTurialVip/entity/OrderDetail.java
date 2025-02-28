package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name="tbl_orderdetail")
public class OrderDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Integer id;

	private String firstName;

	private String lastName;

	private String email;

	private String mobileNo;


}
