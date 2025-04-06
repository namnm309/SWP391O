package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.entity.ProductAgeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAgeGroupRepository extends JpaRepository<ProductAgeGroup, Long> {
    // Các phương thức tùy chỉnh nếu cần (ví dụ: tìm theo product)
    void deleteByProductId(Long productId);  // Xóa tất cả ProductAgeGroup có product_id cụ thể

}
