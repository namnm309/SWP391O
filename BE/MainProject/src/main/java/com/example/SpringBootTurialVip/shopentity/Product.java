package com.example.SpringBootTurialVip.shopentity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="tbl_product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 500,unique = true)
	private String title;

	@Column(length = 5000)
	private String description;

	@Column(unique = true)
	private String category;

	private Double price;

	private int stock;

	private String image;

	private int discount;
	
	private Double discountPrice;
	
	private Boolean isActive;
	
}
