package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.entity.Product;
import com.example.SpringBootTurialVip.entity.ProductAgeGroup;
import com.example.SpringBootTurialVip.enums.AgeGroup;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductResponse {

    private Long id;
    private String title;
    private String description;
    private double price;
    private int discount;
    private double discountPrice;
    private Integer quantity;
    private Boolean isActive;
    private Boolean isPriority;

    private Integer numberOfDoses;
    private Integer minAgeMonths;
    private Integer maxAgeMonths;
    private Integer minDaysBetweenDoses;

    private String manufacturer;
    private List<AgeGroup> targetGroup;  // Liên kết với nhiều AgeGroup
    private String schedule;
    private String sideEffects;

    private List<String> imageList;

    private Long categoryId;
    private String categoryName;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.discount = product.getDiscount();
        this.discountPrice = product.getDiscountPrice();
        this.quantity = product.getQuantity();
        this.isActive = product.getIsActive();
        this.isPriority = product.getIsPriority();
        this.numberOfDoses = product.getNumberOfDoses();
        this.minAgeMonths = product.getMinAgeMonths();
        this.maxAgeMonths = product.getMaxAgeMonths();
        this.minDaysBetweenDoses = product.getMinDaysBetweenDoses();
        this.manufacturer = product.getManufacturer();

        this.targetGroup = product.getTargetGroup().stream()
                .map(ProductAgeGroup::getAgeGroup)
                .collect(Collectors.toList());

        this.schedule = product.getSchedule();
        this.sideEffects = product.getSideEffects();

        this.imageList = product.getImageList();

        if (product.getCategory() != null) {
            this.categoryId = product.getCategory().getId();
            this.categoryName = product.getCategory().getName();
        }
    }
}

