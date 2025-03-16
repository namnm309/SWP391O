package com.example.SpringBootTurialVip.repository;


import com.example.SpringBootTurialVip.dto.response.RevenueResponse;
import com.example.SpringBootTurialVip.dto.response.VaccinationHistoryResponse;
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

//  @Query("SELECT COUNT(o) FROM tbl_productorder o WHERE o.order_date >= CURRENT_DATE - 30")
//  long countOrdersLast30Days();

	/**
	 * Tính số lượng vaccine trung bình được đặt mỗi ngày
	 */
//  @Query("SELECT AVG(od.quantity) FROM OrderDetail od WHERE od.productOrder.status = 'COMPLETED'")
//  Double getAverageDailyOrders();



	/**
	 * Lấy tên vaccine được tiêm nhiều nhất trong tháng hiện tại
	 */
//  @Query("SELECT p.title FROM ProductOrder po JOIN po.product p " +
//        "WHERE EXTRACT(MONTH FROM po.orderDate) = EXTRACT(MONTH FROM CURRENT_DATE) " +
//        "AND EXTRACT(YEAR FROM po.orderDate) = EXTRACT(YEAR FROM CURRENT_DATE) " +
//        "GROUP BY p.title ORDER BY COUNT(po) DESC LIMIT 1")
//  String getTopVaccineOfMonth();


	/**
	 * Lấy tổng doanh thu trong khoảng thời gian nhất định
	 */
	@Query("SELECT new com.example.SpringBootTurialVip.dto.response.RevenueResponse(SUM(po.totalPrice)) " +
			"FROM ProductOrder po " +
			"WHERE po.orderDate BETWEEN :startDate AND :endDate")
	RevenueResponse getRevenue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	/**
	 * Độ tuổi của trẻ được tiêm nhiều nhất
	 */
	@Query("SELECT EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM u.bod) " +
			"FROM ProductOrder po JOIN po.user u " +
			"GROUP BY u.bod ORDER BY COUNT(po) DESC LIMIT 1")
	Integer getMostVaccinatedAge();

	@Query(value = """
        SELECT p.title AS vaccineName, COUNT(od.id) AS totalOrders
        FROM tbl_orderdetail od
        JOIN tbl_productorder po ON od.order_id = po.order_id
        JOIN tbl_product p ON od.product_id = p.id
        WHERE EXTRACT(MONTH FROM po.order_date) = :month
        AND EXTRACT(YEAR FROM po.order_date) = :year
        GROUP BY p.title
        ORDER BY totalOrders DESC
        LIMIT 5
        """, nativeQuery = true)
	List<VaccineOrderStats> findTopVaccinesByMonthAndYear(@Param("month") int month, @Param("year") int year);


	@Query(value = """
    SELECT p.title AS vaccineName, COUNT(od.id) AS totalOrders
    FROM tbl_orderdetail od
    JOIN tbl_productorder po ON od.order_id = po.order_id
    JOIN tbl_product p ON od.product_id = p.id
    WHERE EXTRACT(MONTH FROM po.order_date) = :month
    AND EXTRACT(YEAR FROM po.order_date) = :year
    GROUP BY p.title
    ORDER BY totalOrders ASC
    LIMIT 5
    """, nativeQuery = true)
	List<VaccineOrderStats> findLeastOrderedVaccines(@Param("month") int month, @Param("year") int year);


	List<ProductOrder> findAll();

	//ProductOrder findById(Long id);


	List<ProductOrder> findByStatus(String status);

	//List<ProductOrder> findByStatusId(Integer statusId);

	//Optional<ProductOrder> findById(Long id);


//  @Query("SELECT po FROM ProductOrder po ORDER BY po.orderDate DESC LIMIT 1")
//  Optional<ProductOrder> findTopByOrderByOrderDateDesc();

	//Optional<ProductOrder> findByOrderId(String orderId);

	//ProductOrder findByOrderId(String orderId);

	@Query(value = """
        SELECT p.title AS vaccineName, COUNT(od.id) AS totalDoses
        FROM tbl_orderdetail od
        JOIN tbl_productorder po ON od.order_id = po.order_id
        JOIN tbl_product p ON od.product_id = p.id
        WHERE po.order_date >= :startDate
        GROUP BY p.title
        ORDER BY totalDoses DESC
        LIMIT 1
        """, nativeQuery = true)
	Object[] findTopVaccineSince(@Param("startDate") LocalDate startDate);






	@Query(value = """
        SELECT COALESCE(SUM(po.total_price), 0) 
        FROM tbl_productorder po
        WHERE po.order_date >= :startDate
        """, nativeQuery = true)
	Double getRevenueSince(@Param("startDate") LocalDate startDate);




	@Query(value = """
        SELECT EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM u.bod) AS age, COUNT(po.id) AS totalDoses
        FROM tbl_productorder po
        JOIN tbl_users u ON po.user_user_id = u.user_id
        WHERE po.order_date >= :startDate
        GROUP BY age
        ORDER BY totalDoses DESC
        LIMIT 1
        """, nativeQuery = true)
	Object[] findMostVaccinatedAgeSince(@Param("startDate") LocalDate startDate);

	@Query(value = """
        SELECT p.title AS vaccineName, COUNT(od.id) AS totalDoses
        FROM tbl_orderdetail od
        JOIN tbl_productorder po ON od.order_id = po.order_id
        JOIN tbl_product p ON od.product_id = p.id
        WHERE po.order_date >= :startDate
        GROUP BY p.title
        ORDER BY totalDoses ASC
        LIMIT 1
        """, nativeQuery = true)
	Object[] findLeastOrderedVaccineSince(@Param("startDate") LocalDate startDate);

//	@Query("SELECT new com.example.SpringBootTurialVip.dto.response.VaccinationHistoryResponse(o) " +
//			"FROM OrderDetail o " +
//			"JOIN o.child c " +
//			"JOIN c.parent p " +
//			"WHERE p.id = :customerId " +
//			"ORDER BY o.vaccinationDate DESC")
//	List<VaccinationHistoryResponse> getVaccinationHistoryByCustomerId(@Param("customerId") Long customerId);

	//Optional<ProductOrder> getOrderByOrderCode(String orderId);


	// Lấy doanh thu từng ngày
	@Query(value = """
    SELECT DATE(po.order_date) AS date, COALESCE(SUM(po.total_price), 0) AS totalRevenue
    FROM tbl_productorder po
    WHERE po.order_date >= CURRENT_DATE - (? * INTERVAL '1 day')
    GROUP BY date
    ORDER BY date ASC
    """, nativeQuery = true)
	List<Object[]> getDailyRevenue(@Param("days") int days);

	// Lấy vaccine được tiêm nhiều nhất từng ngày
	@Query(value = """
    SELECT DATE(po.order_date) AS date, p.title AS vaccineName, COUNT(od.id) AS totalDoses
    FROM tbl_orderdetail od
    JOIN tbl_productorder po ON od.order_id = po.order_id
    JOIN tbl_product p ON od.product_id = p.id
    WHERE po.order_date >= CURRENT_DATE - (? * INTERVAL '1 day')
    GROUP BY date, p.title
    ORDER BY date ASC, totalDoses DESC
    """, nativeQuery = true)
	List<Object[]> getDailyTopVaccine(@Param("days") int days);

	// Lấy vaccine được tiêm ít nhất từng ngày
	@Query(value = """
    SELECT DATE(po.order_date) AS date, p.title AS vaccineName, COUNT(od.id) AS totalDoses
    FROM tbl_orderdetail od
    JOIN tbl_productorder po ON od.order_id = po.order_id
    JOIN tbl_product p ON od.product_id = p.id
    WHERE po.order_date >= CURRENT_DATE - (? * INTERVAL '1 day')
    GROUP BY date, p.title
    ORDER BY date ASC, totalDoses ASC
    """, nativeQuery = true)
	List<Object[]> getDailyLeastOrderedVaccine(@Param("days") int days);

	// Lấy độ tuổi được tiêm nhiều nhất từng ngày
	@Query(value = """
    SELECT DATE(po.order_date) AS date, EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM u.bod) AS age, COUNT(po.id) AS totalDoses
    FROM tbl_productorder po
    JOIN users u ON po.user_user_id = u.user_id
    WHERE po.order_date >= CURRENT_DATE - (? * INTERVAL '1 day')
    GROUP BY date, age
    ORDER BY date ASC, totalDoses DESC
    """, nativeQuery = true)
	List<Object[]> getDailyMostVaccinatedAge(@Param("days") int days);


}

