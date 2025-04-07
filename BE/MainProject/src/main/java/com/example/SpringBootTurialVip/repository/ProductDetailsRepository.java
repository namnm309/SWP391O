package com.example.SpringBootTurialVip.repository;

import com.example.SpringBootTurialVip.entity.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Long> {

    @Query("SELECT pd FROM ProductDetails pd WHERE pd.expirationDate < :currentDate")
    List<ProductDetails> findExpiredProducts(@Param("currentDate") LocalDate currentDate);

    List<ProductDetails> findByProductId(Long productId);

    List<ProductDetails> findBySku(String sku); // Giữ lại phương thức này với Optional

    // Tìm tất cả các bản ghi có sku trùng khớp (trả về danh sách, không phải Optional)
//    List<ProductDetails> findBySkuList(String sku); // Phương thức mới trả về danh sách

    List<ProductDetails> findByBatchNumber(String batchNumber);

    List<ProductDetails> findBySkuAndBatchNumber(String sku, String batchNumber);

    @Query("SELECT pd FROM ProductDetails pd WHERE pd.sku = :sku OR pd.batchNumber = :batchNumber")
    List<ProductDetails> findBySkuOrBatch(@Param("sku") String sku, @Param("batchNumber") String batchNumber);
}

