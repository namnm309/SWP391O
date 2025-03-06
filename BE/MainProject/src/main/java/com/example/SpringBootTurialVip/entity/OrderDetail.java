package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

	private Long childid;

	@OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL)
	private List<Reaction> reactions;


}
