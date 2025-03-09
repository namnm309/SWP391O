package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="tbl_productorder")
public class ProductOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String orderId;

	private LocalDate orderDate;

	@ManyToOne
	private Product product;

	private Double price;

	private Integer quantity;

	@ManyToOne
	@JoinColumn(name = "user_user_id", referencedColumnName = "user_id", nullable = false)
	private User user;

	private String status;

	private String paymentType;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_detail_id", referencedColumnName = "id", nullable = false)
	//Giúp xác định gán order_detail_id là khóa ngoại vào bảng productorder và trỏ tới id của bảng orderdetail
	private OrderDetail orderDetail;

}
