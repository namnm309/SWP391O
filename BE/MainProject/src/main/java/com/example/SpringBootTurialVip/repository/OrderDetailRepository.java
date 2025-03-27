package com.example.SpringBootTurialVip.repository;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.UpcomingVaccinationResponse;
import com.example.SpringBootTurialVip.dto.response.VaccinationHistoryResponse;
import com.example.SpringBootTurialVip.entity.OrderDetail;
import com.example.SpringBootTurialVip.enums.OrderDetailStatus;
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
//    @Query(value = """
//        SELECT new com.example.SpringBootTurialVip.dto.response.VaccinationHistoryResponse(
//            od.id, p.title, od.vaccinationDate, od.quantity
//        )
//        FROM OrderDetail od
//        JOIN od.product p
//        JOIN od.child u
//        JOIN ProductOrder po ON od.orderId = po.orderId
//        WHERE u.id = :childId
//        AND po.status = 'Success'
//        AND od.vaccinationDate <= CURRENT_TIMESTAMP
//        ORDER BY od.vaccinationDate DESC
//        """)
    @Query(value = """
    SELECT new com.example.SpringBootTurialVip.dto.response.VaccinationHistoryResponse(
        od.id, p.title, od.vaccinationDate, od.quantity
    ) 
    FROM OrderDetail od
    JOIN od.product p
    JOIN od.child u
    WHERE u.id = :childId
    AND od.status = 'DA_TIEM'
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
    //=============================================================================
     //Đếm số mũi đã từng đặt (tính tất cả các đơn)
    @Query("""
    SELECT COUNT(od) FROM OrderDetail od
    WHERE od.product.id = :productId
    AND od.child.id = :childId
    AND od.status = 'DA_TIEM'
""")
    int countDosesTaken(@Param("productId") Long productId, @Param("childId") Long childId);

//Check sắp tới chặn đặt dư
//    @Query("""
//    SELECT COUNT(od) FROM OrderDetail od
//    WHERE od.product.id = :productId
//    AND od.child.id = :childId
//    AND od.status IN ('CHUA_TIEM', 'DA_LEN_LICH')
//""")
//    int countUpcomingDoses(@Param("productId") Long productId, @Param("childId") Long childId);


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

    @Query("""
    SELECT COUNT(od) > 0 FROM OrderDetail od
    WHERE od.child.id = :childId AND od.vaccinationDate = :vaccinationDate
""")
    boolean existsByChildIdAndVaccinationDate(@Param("childId") Long childId,
                                              @Param("vaccinationDate") LocalDateTime vaccinationDate);

    //Tìm đơn in ngày có status
    @Query("""
    SELECT od FROM OrderDetail od
    ORDER BY od.vaccinationDate
""")
    List<OrderDetail> findAllForUpcoming();




    @Query("""
    SELECT od FROM OrderDetail od
    WHERE od.vaccinationDate BETWEEN :startDate AND :endDate
    AND (od.status = 'CHUA_TIEM' OR od.status = 'DA_LEN_LICH')
    ORDER BY od.vaccinationDate ASC
""")
    List<OrderDetail> findSchedulesByWeek(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    //Ls các mũi đã tiêm v1
//    @Query("""
//    SELECT new com.example.SpringBootTurialVip.dto.response.ChildWithInjectionInfoResponse.InjectionInfo(
//        p.title, od.vaccinationDate, od.reactionNote
//    )
//    FROM OrderDetail od
//    JOIN od.product p
//    JOIN ProductOrder po ON od.orderId = po.orderId
//    WHERE od.child.id = :childId
//    AND od.status = 'DA_TIEM'
//    AND po.status = 'SUCCESS'
//""")
//    List<ChildResponse.ChildWithInjectionInfoResponse.InjectionInfo> getInjectionHistoryForChild(@Param("childId") Long childId);

    //ds các mũi đã tiêm v2
    @Query("""
SELECT od FROM OrderDetail od
JOIN FETCH od.product
LEFT JOIN FETCH od.reactions
WHERE od.child.id = :childId AND od.status = 'DA_TIEM'
""")
    List<OrderDetail> getVaccinatedDetailsWithReactions(@Param("childId") Long childId);


//    @Query("""
//    SELECT od FROM OrderDetail od
//    JOIN FETCH od.child c
//    JOIN FETCH od.product p
//    WHERE od.vaccinationDate >= :fromDate
//    AND (:status IS NULL OR od.status = :status)
//    ORDER BY od.vaccinationDate ASC
//""")
//    List<OrderDetail> findUpcomingSchedules(@Param("fromDate") LocalDateTime fromDate,
//                                            @Param("status") String status);

    @Query("""
    SELECT od FROM OrderDetail od
    JOIN FETCH od.child c
    JOIN FETCH od.product p
    WHERE od.vaccinationDate >= :fromDate
    AND (:status IS NULL OR od.status = :status)
    ORDER BY od.vaccinationDate ASC
""")
    List<OrderDetail> findUpcomingSchedules(@Param("fromDate") LocalDateTime fromDate,
                                            @Param("status") OrderDetailStatus status);




    @Query("""
    SELECT od FROM OrderDetail od
    JOIN FETCH od.child c
    JOIN FETCH od.product p
    WHERE c.parentid = :parentId
    AND od.vaccinationDate >= :fromDate
    AND (:status IS NULL OR od.status = :status)
    ORDER BY od.vaccinationDate ASC
""")
    List<OrderDetail> findUpcomingSchedulesForParent(@Param("parentId") Long parentId,
                                                     @Param("fromDate") LocalDateTime fromDate,
                                                     @Param("status") OrderDetailStatus status); // Dùng ENUM trực tiếp


    boolean existsByChildIdAndProductIdAndVaccinationDate(Long childId, Long productId, LocalDateTime vaccinationDate);




}
