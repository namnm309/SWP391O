package com.example.SpringBootTurialVip.entity;

import com.example.SpringBootTurialVip.enums.AgeGroup;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbl_product_details")
public class ProductDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // Liên kết với Product

    @Column(nullable = false)
    private String sku;  // Mã SKU của sản phẩm

    @Column(nullable = false)
    private String batchNumber;  // Số lô

    @Column(nullable = false)
    private LocalDate expirationDate;  // Ngày hết hạn

    @Column(nullable = false)
    private Integer quantity;  // Số lượng trong lô

    private Integer reservedQuantity = 0;  // Số lượng đã được đặt (chưa tiêm)

    private Boolean isActive = true;  // Trạng thái của sản phẩm (có thể dùng để đánh dấu hết hạn)

    // Liên kết với ProductAgeGroup
    @OneToMany(mappedBy = "productDetails", cascade = CascadeType.ALL)
    private List<ProductAgeGroup> productAgeGroups;  // Liên kết với các nhóm tuổi phù hợp

    // Hàm cập nhật reservedQuantity khi có đơn đặt
    public void updateReservedQuantity(int quantity) {
        // Kiểm tra và cập nhật reservedQuantity cho từng đơn đặt
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        this.reservedQuantity += quantity;
        this.product.updateQuantities();  // Cập nhật lại số lượng tổng trong Product
    }

    // Hàm giảm reservedQuantity khi khách tiêm thành công
    public void reduceReservedQuantity(int quantity) {
        if (quantity <= 0 || this.reservedQuantity < quantity) {
            throw new IllegalArgumentException("Số lượng giảm không hợp lệ");
        }
        this.reservedQuantity -= quantity;
        this.product.updateQuantities();  // Cập nhật lại số lượng tổng trong Product
    }

    // Kiểm tra xem sản phẩm này có hết hạn hay không
    public boolean isExpired() {
        return expirationDate != null && expirationDate.isBefore(LocalDate.now());
    }

    // Kiểm tra tính hợp lệ của các trường thông tin quan trọng
    public boolean isValid() {
        return sku != null && !sku.isEmpty() &&
                batchNumber != null && !batchNumber.isEmpty() &&
                expirationDate != null && quantity != null && quantity > 0;
    }
}


