package com.example.SpringBootTurialVip.repository;


import com.example.SpringBootTurialVip.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

	@Query(value = "SELECT * FROM tbl_productorder WHERE user_user_id = :userId", nativeQuery = true)
	List<ProductOrder> findByUserId(@Param("userId") Long userId);

	ProductOrder findByOrderId(String orderId);

//	@Query("SELECT COUNT(o) FROM tbl_productorder o WHERE o.order_date >= CURRENT_DATE - 30")
//	long countOrdersLast30Days();


}
