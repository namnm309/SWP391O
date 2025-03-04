package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

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

	// Chỉ ánh xạ `id` của Category
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	// Chỉ lấy `name` của Category mà không lưu vào DB
	@Transient
	private String categoryName;

	// Phương thức để lấy `name` của Category từ đối tượng liên kết
	public String getCategoryName() {
		return category != null ? category.getName() : null;
	}

	private Double price;

	private int stock;

	private String image;

	private int discount;

	private Double discountPrice;

	private Boolean isActive = true;

	@Column(nullable = false)
	private String manufacturer; // Nhà sản xuất

	@Column(nullable = false)
	private String targetGroup; // Đối tượng tiêm

	@Column(nullable = false, length = 1000)
	private String schedule; // Phác đồ, lịch tiêm

	@Column(nullable = false, length = 500)
	private String sideEffects; // Phản ứng sau tiêm

	@Column(nullable = false)
	private boolean available; // Tình trạng có sẵn

	@Column
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column
	private LocalDateTime updatedAt;
}
