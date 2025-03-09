package com.example.SpringBootTurialVip.repository;


import com.example.SpringBootTurialVip.dto.response.RevenueResponse;
import com.example.SpringBootTurialVip.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

	@Query(value = "SELECT * FROM tbl_productorder WHERE user_user_id = :userId", nativeQuery = true)
	List<ProductOrder> findByUserId(@Param("userId") Long userId);

	ProductOrder findByOrderId(String orderId);

//	@Query("SELECT COUNT(o) FROM tbl_productorder o WHERE o.order_date >= CURRENT_DATE - 30")
//	long countOrdersLast30Days();

	/**
	 * Tính số lượng vaccine trung bình được đặt mỗi ngày
	 */
	@Query("SELECT AVG(po.quantity) FROM ProductOrder po WHERE po.status = 'COMPLETED'")
	Double getAverageDailyOrders();

	/**
	 * Lấy tên vaccine được tiêm nhiều nhất trong tháng hiện tại
	 */
	@Query("SELECT p.title FROM ProductOrder po JOIN po.product p " +
			"WHERE EXTRACT(MONTH FROM po.orderDate) = EXTRACT(MONTH FROM CURRENT_DATE) " +
			"AND EXTRACT(YEAR FROM po.orderDate) = EXTRACT(YEAR FROM CURRENT_DATE) " +
			"GROUP BY p.title ORDER BY COUNT(po) DESC LIMIT 1")
	String getTopVaccineOfMonth();


	/**
	 * Lấy tổng doanh thu trong khoảng thời gian nhất định
	 */
	@Query("SELECT new com.example.SpringBootTurialVip.dto.response.RevenueResponse(SUM(po.price)) " +
			"FROM ProductOrder po WHERE po.orderDate BETWEEN :startDate AND :endDate")
	RevenueResponse getRevenue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	/**
	 * Độ tuổi của trẻ được tiêm nhiều nhất
	 */
	@Query("SELECT EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM u.bod) " +
			"FROM ProductOrder po JOIN po.user u " +
			"GROUP BY u.bod ORDER BY COUNT(po) DESC LIMIT 1")
	Integer getMostVaccinatedAge();

	@Query(value = """
        SELECT p.title AS vaccineName, COUNT(po.id) AS totalOrders 
        FROM tbl_productorder po 
        JOIN tbl_product p ON po.product_id = p.id 
        WHERE EXTRACT(MONTH FROM po.order_date) = :month 
        AND EXTRACT(YEAR FROM po.order_date) = :year 
        GROUP BY p.title 
        ORDER BY totalOrders DESC 
        LIMIT 5
        """, nativeQuery = true)
	List<VaccineOrderStats> findTopVaccinesByMonthAndYear(@Param("month") int month, @Param("year") int year);

	@Query(value = """
    SELECT p.title AS vaccineName, COUNT(po.id) AS totalOrders 
    FROM tbl_productorder po 
    JOIN tbl_product p ON po.product_id = p.id 
    WHERE EXTRACT(MONTH FROM po.order_date) = :month 
    AND EXTRACT(YEAR FROM po.order_date) = :year 
    GROUP BY p.title 
    ORDER BY totalOrders ASC 
    LIMIT 5
    """, nativeQuery = true)
	List<VaccineOrderStats> findLeastOrderedVaccines(@Param("month") int month, @Param("year") int year);

	List<ProductOrder> findAll();

	Optional<ProductOrder> findById(Long id);


	List<ProductOrder> findByStatus(String status);

	//List<ProductOrder> findByStatusId(Integer statusId);

	//Optional<ProductOrder> findById(Long id);


	@Query("SELECT po FROM ProductOrder po ORDER BY po.orderDate DESC LIMIT 1")
	Optional<ProductOrder> findTopByOrderByOrderDateDesc();


}
