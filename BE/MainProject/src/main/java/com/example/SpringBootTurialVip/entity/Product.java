package com.example.SpringBootTurialVip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	@Column(nullable = true, length = 5000)
	private String image;

	private int discount;

	private Double discountPrice;

	private Boolean isActive = true;

	@Column(nullable = false,length = 5000)
	private String manufacturer; // Nhà sản xuất

	@Column(nullable = false,length = 5000)
	private String targetGroup; // Đối tượng tiêm

	@Column(nullable = false,length = 5000)
	private String schedule; // Phác đồ, lịch tiêm

	@Column(nullable = false,length = 5000)
	private String sideEffects; // Phản ứng sau tiêm

	@Column(nullable = false)
	private boolean available; // Tình trạng có sẵn

	@Column
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column
	private LocalDateTime updatedAt;

	// Chuyển đổi chuỗi imageUrls thành danh sách List<String>
	public List<String> getImageList() {
		if (image == null || image.isEmpty()) {
			return new ArrayList<>();
		}
		return Arrays.asList(image.split(",")); // Chuyển chuỗi thành danh sách
	}

	// Chuyển đổi danh sách List<String> thành chuỗi imageUrls
	public void setImageList(List<String> imageList) {
		if (imageList == null || imageList.isEmpty()) {
			this.image = null;
		} else {
			this.image = String.join(",", imageList); // Chuyển danh sách thành chuỗi
		}
	}

	// Độ tuổi nhỏ nhất được tiêm
	private Integer minAgeMonths;

	// Độ tuổi lớn nhất được tiêm
	private Integer maxAgeMonths;

	// Tổng số mũi cần tiêm
	private Integer numberOfDoses;

	// Số ngày tối thiểu giữa các mũi
	private Integer minDaysBetweenDoses;

	// Tổng số liều vaccine còn lại trong kho
	private Integer quantity;

	// Số liều đã được khách đặt nhưng chưa tiêm (đã đặt lịch)
	private Integer reservedQuantity;

	//Hàm tính tuổi
	public int calculateAgeInMonths(LocalDate dateOfBirth) {
		Period age = Period.between(dateOfBirth, LocalDate.now());
		return age.getYears() * 12 + age.getMonths();
	}


}
