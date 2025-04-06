package com.example.SpringBootTurialVip.entity;

import com.example.SpringBootTurialVip.enums.AgeGroup;
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

	//private int stock;

	@Column(nullable = true, length = 5000)
	private String image;

	private Integer discount;

	private Double discountPrice;

	private Boolean isActive = true;

	@Column(nullable = false,length = 5000)
	private String manufacturer; // Nhà sản xuất

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
	private Integer reservedQuantity=0;

	//Hàm tính tuổi
	public int calculateAgeInMonths(LocalDate dateOfBirth) {
		Period age = Period.between(dateOfBirth, LocalDate.now());
		return age.getYears() * 12 + age.getMonths();
	}

	@Column(name = "is_priority")
	private Boolean isPriority = false;

	//============================================================================================================================================

	//private String sku;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductDetails> productDetails;

	// Liên kết với bảng phụ ProductAgeGroup
	@OneToMany(mappedBy = "product")
	private List<ProductAgeGroup> targetGroup;  // Liên kết với ProductAgeGroup


	// Phương thức tự động phân loại AgeGroup dựa trên minAgeMonths và maxAgeMonths
	public void updateAgeGroupFromAge() {
		this.targetGroup = determineAgeGroups(this.minAgeMonths, this.maxAgeMonths);
	}

	// Tính toán các AgeGroup phù hợp với độ tuổi min và max
	private List<ProductAgeGroup> determineAgeGroups(int minAgeMonths, int maxAgeMonths) {
		List<ProductAgeGroup> ageGroups = new ArrayList<>();

		if (minAgeMonths >= 0 && maxAgeMonths <= 3) {
			ageGroups.add(new ProductAgeGroup(this, AgeGroup.AGE_0_3));
		}
		if (minAgeMonths >= 4 && maxAgeMonths <= 6) {
			ageGroups.add(new ProductAgeGroup(this, AgeGroup.AGE_4_6));
		}
		if (minAgeMonths >= 7 && maxAgeMonths <= 12) {
			ageGroups.add(new ProductAgeGroup(this, AgeGroup.AGE_7_12));
		}
		if (minAgeMonths >= 13 && maxAgeMonths <= 24) {
			ageGroups.add(new ProductAgeGroup(this, AgeGroup.AGE_13_24));
		}
		if (minAgeMonths >= 25) {
			ageGroups.add(new ProductAgeGroup(this, AgeGroup.AGE_25_PLUS));
		}

		return ageGroups;
	}

	// Phương thức để cập nhật quantity và reservedQuantity
// Phương thức để cập nhật quantity và reservedQuantity
	public void updateQuantities() {
		int totalQuantity = 0;
		int totalReserved = 0;

		// Duyệt qua các ProductDetails và tính tổng số lượng và số lượng đã đặt
		for (ProductDetails productDetails : productDetails) {
			if (productDetails.getIsActive()) {
				totalQuantity += productDetails.getQuantity();  // Cộng dồn số lượng hợp lệ
				totalReserved += productDetails.getReservedQuantity();  // Cộng dồn reservedQuantity
			}
		}

		this.quantity = totalQuantity - totalReserved;  // Số lượng còn lại
		this.reservedQuantity = totalReserved;  // Số lượng đã đặt
	}

	@ElementCollection
	@CollectionTable(name = "tbl_product_underlying_conditions", joinColumns = @JoinColumn(name = "product_id"))
	@Column(name = "condition_name")
	private List<String> underlyingConditions;  // Liên kết với danh sách bệnh nền (ví dụ: "Tiểu đường", "Huyết áp cao")






}
