package com.example.SpringBootTurialVip.entity;

import com.example.SpringBootTurialVip.enums.AgeGroup;
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
@Table(name = "tbl_product_age_groups")
public class ProductAgeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;  // Liên kết với Product

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;  // Liên kết với AgeGroup enum

    @ManyToOne
    @JoinColumn(name = "product_details_id", referencedColumnName = "id")
    private ProductDetails productDetails; // Thêm thuộc tính liên kết với ProductDetails

    public ProductAgeGroup(Product product, AgeGroup ageGroup) {
        this.product = product;
        this.ageGroup = ageGroup;
    }

    public ProductAgeGroup(Product product, AgeGroup ageGroup, ProductDetails productDetails) {
        this.product = product;
        this.ageGroup = ageGroup;
        this.productDetails = productDetails; // Khởi tạo liên kết với ProductDetails
    }

}