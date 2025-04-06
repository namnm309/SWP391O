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

    Optional<ProductDetails> findBySku(String sku);


}
