package com.example.SpringBootTurialVip.repository;
import com.example.SpringBootTurialVip.dto.response.UpcomingVaccinationResponse;
import com.example.SpringBootTurialVip.dto.response.VaccinationHistoryResponse;
import com.example.SpringBootTurialVip.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    // Lấy danh sách OrderDetail theo orderId
    List<OrderDetail> findByOrderId(String orderId);

    Optional<OrderDetail> findFirstByOrderId(String orderId);


    // Đếm số mũi tiêm của một sản phẩm trong một đơn hàng
    int countByOrderIdAndProductId(String orderId, Long productId);

    @Query("SELECT AVG(od.quantity) FROM OrderDetail od")
    Double getAverageDailyOrders();

    //Vaccine đc tiêm nhiều nhất trong tháng
    @Query("SELECT p.title FROM OrderDetail od " +
            "JOIN Product p ON od.product.id = p.id " +
            "JOIN ProductOrder po ON od.orderId = po.orderId " +
            "WHERE EXTRACT(MONTH FROM po.orderDate) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "AND EXTRACT(YEAR FROM po.orderDate) = EXTRACT(YEAR FROM CURRENT_DATE) " +
            "GROUP BY p.title " +
            "ORDER BY COUNT(od.id) DESC " +
            "LIMIT 1")
    String getTopVaccineOfMonth();



    //Ls tiêm chủng
    @Query(value = """
        SELECT new com.example.SpringBootTurialVip.dto.response.VaccinationHistoryResponse(
            od.id, p.title, od.vaccinationDate, od.quantity
        ) 
        FROM OrderDetail od
        JOIN od.product p
        JOIN od.child u
        JOIN ProductOrder po ON od.orderId = po.orderId
        WHERE u.id = :childId
        AND po.status = 'Success'
        AND od.vaccinationDate <= CURRENT_TIMESTAMP 
        ORDER BY od.vaccinationDate DESC
        """)
    List<VaccinationHistoryResponse> getVaccinationHistory(@Param("childId") Long childId);

    //Lịch tiêm tiếp theo
    @Query(value = """
        SELECT new com.example.SpringBootTurialVip.dto.response.UpcomingVaccinationResponse(
            od.id, p.title, od.vaccinationDate, od.quantity
        ) 
        FROM OrderDetail od
        JOIN od.product p
        JOIN od.child u
        WHERE u.id = :childId
        AND od.vaccinationDate >= CURRENT_TIMESTAMP
        ORDER BY od.vaccinationDate ASC
        """)
    List<UpcomingVaccinationResponse> getUpcomingVaccinations(@Param("childId") Long childId);

    // Đếm số mũi đã tiêm thành công
//    @Query("SELECT COUNT(od) FROM OrderDetail od " +
//            "JOIN ProductOrder po ON od.orderId = po.orderId " +
//            "WHERE od.product.id = :productId " +
//            "AND od.child.id = :childId " +
//            "AND po.status = 'Success' " +
//            "AND od.vaccinationDate <= CURRENT_TIMESTAMP")
//    int countByProductIdAndChildIdAndVaccinationDateBefore(Long productId, Long childId, LocalDateTime now);
//
//    // Lấy ngày tiêm gần nhất
//    @Query("SELECT MAX(od.vaccinationDate) FROM OrderDetail od " +
//            "JOIN ProductOrder po ON od.orderId = po.orderId " +
//            "WHERE od.product.id = :productId " +
//            "AND od.child.id = :childId " +
//            "AND po.status = 'Success' " +
//            "AND od.vaccinationDate <= CURRENT_TIMESTAMP")
//    Optional<LocalDateTime> findLastVaccinationDate(Long productId, Long childId);

    //===============================================================================================

//    // Lấy ngày đặt gần nhất của 1 sản phẩm mà bé đã từng đặt (qua đơn hàng đã thanh toán)
//    @Query("""
//    SELECT MAX(po.orderDate)
//    FROM OrderDetail od
//    JOIN ProductOrder po ON od.orderId = po.orderId
//    WHERE od.product.id = :productId
//    AND od.child.id = :childId
//
//""")
//    Optional<LocalDate> findLastBookingDate(Long productId, Long childId);

    // Đếm tổng số mũi đã đặt trước đó (chỉ tính đơn thành công)
//    @Query("""
//    SELECT COUNT(od)
//    FROM OrderDetail od
//    JOIN ProductOrder po ON od.orderId = po.orderId
//    WHERE od.product.id = :productId
//    AND od.child.id = :childId
//
//""")
//    int countDosesTaken(Long productId, Long childId);

    //=============================================================================
     //Đếm số mũi đã từng đặt (tính tất cả các đơn)
    @Query("""
    SELECT COUNT(od) FROM OrderDetail od
    WHERE od.product.id = :productId
    AND od.child.id = :childId
""")
    int countDosesTaken(Long productId, Long childId);

    // Lấy ngày đặt gần nhất
    @Query("""
    SELECT MAX(po.orderDate)
    FROM OrderDetail od
    JOIN ProductOrder po ON od.orderId = po.orderId
    WHERE od.product.id = :productId
    AND od.child.id = :childId
""")
    Optional<LocalDate> findLastBookingDate(Long productId, Long childId);

    //Check order qua han
    @Query("""
        SELECT od FROM OrderDetail od
        WHERE od.vaccinationDate < :now
        AND od.status = 'DA_LEN_LICH'
    """)
    List<OrderDetail> findOverdueVaccines(LocalDateTime now);





}
