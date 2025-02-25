package com.example.SpringBootTurialVip.shoprepository;


import com.example.SpringBootTurialVip.shopentity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

	@Query(value = "SELECT * FROM tbl_productorder WHERE user_user_id = :userId", nativeQuery = true)
	List<ProductOrder> findByUserId(@Param("userId") Long userId);

	ProductOrder findByOrderId(String orderId);

}
