package com.example.SpringBootTurialVip.repository;


import com.example.SpringBootTurialVip.dto.response.RevenueResponse;
import com.example.SpringBootTurialVip.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

	@Query(value = "SELECT * FROM tbl_productorder WHERE user_user_id = :userId", nativeQuery = true)
	List<ProductOrder> findByUserId(@Param("userId") Long userId);

	ProductOrder findByOrderId(String orderId);

//	@Query("SELECT COUNT(o) FROM tbl_productorder o WHERE o.order_date >= CURRENT_DATE - 30")
//	long countOrdersLast30Days();

	/**
	 * 1️⃣ Tính số lượng vaccine trung bình được đặt mỗi ngày
	 */
	@Query("SELECT AVG(po.quantity) FROM ProductOrder po WHERE po.status = 'COMPLETED'")
	Double getAverageDailyOrders();

	/**
	 * 2️⃣ Lấy tên vaccine được tiêm nhiều nhất trong tháng hiện tại
	 */
	@Query("SELECT p.title FROM ProductOrder po JOIN po.product p " +
			"WHERE FUNCTION('MONTH', po.orderDate) = FUNCTION('MONTH', CURRENT_DATE) " +
			"AND FUNCTION('YEAR', po.orderDate) = FUNCTION('YEAR', CURRENT_DATE) " +
			"GROUP BY p.title ORDER BY COUNT(po) DESC LIMIT 1")
	String getTopVaccineOfMonth();

	/**
	 * 3️⃣ Lấy tổng doanh thu trong khoảng thời gian nhất định
	 */
	@Query("SELECT new com.example.SpringBootTurialVip.dto.response.RevenueResponse(SUM(po.price)) " +
			"FROM ProductOrder po WHERE po.orderDate BETWEEN :startDate AND :endDate")
	RevenueResponse getRevenue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	/**
	 * 4️⃣ Độ tuổi của trẻ được tiêm nhiều nhất
	 */
	@Query("SELECT EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM u.bod) " +
			"FROM ProductOrder po JOIN po.user u " +
			"GROUP BY u.bod ORDER BY COUNT(po) DESC LIMIT 1")
	Integer getMostVaccinatedAge();




}
