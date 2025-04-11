package com.example.SpringBootTurialVip.entity;

import com.example.SpringBootTurialVip.enums.OrderDetailStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

	@ManyToOne
	@JoinColumn(name = "child_id", referencedColumnName = "user_id", nullable = false)
	private User child; // Trỏ đến User (tức là User đại diện cho đứa trẻ)

	@OneToMany(mappedBy = "orderDetail", cascade = CascadeType.ALL)
	private List<Reaction> reactions;

	//==================================================================================================================

	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
	private Product product; // Mỗi OrderDetail chỉ chứa 1 sản phẩm

	private Integer quantity;

	private String orderId; // Sử dụng orderId thay vì khóa ngoại

	@Column(nullable = true)
	private LocalDateTime vaccinationDate;

	// Lấy giá của sản phẩm
	public double getPrice() {
		return product.getDiscountPrice() * quantity;
	}

	@Enumerated(EnumType.STRING)
    private OrderDetailStatus status;

	// Sử dụng để lưu lô đã chọn (ProductDetail)
	@ManyToOne
	@JoinColumn(name = "product_detail_id", referencedColumnName = "id", nullable = false)
	private ProductDetails productDetails; // Trỏ tới ProductDetail đã chọn cho OrderDetail

	// Thêm trường để lưu bác sĩ tiêm (doctor)
	@ManyToOne
	@JoinColumn(name = "doctor_id", referencedColumnName = "user_id", nullable = false)
	private User staffid;  // Trỏ tới User (bác sĩ tiêm) đã xử lý đơn hàng

	@Column(name = "staff_name")
	private String staffName; // Lưu tên bác sĩ tiêm (dành cho hiển thị)



}
