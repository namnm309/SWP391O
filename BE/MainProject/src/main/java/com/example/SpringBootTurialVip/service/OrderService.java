package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.dto.request.OrderRequest;
import com.example.SpringBootTurialVip.dto.response.OrderDetailResponse;
import com.example.SpringBootTurialVip.dto.response.ProductSuggestionResponse;
import com.example.SpringBootTurialVip.dto.response.UpcomingVaccinationResponse;
import com.example.SpringBootTurialVip.dto.response.VaccinationHistoryResponse;
import com.example.SpringBootTurialVip.entity.OrderDetail;
import com.example.SpringBootTurialVip.entity.ProductOrder;
import com.example.SpringBootTurialVip.enums.OrderDetailStatus;
import com.example.SpringBootTurialVip.repository.VaccineOrderStats;
import org.springframework.data.domain.Page;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderService {


//    public void saveOrderByProductId(Long productId, OrderRequest orderRequest, Long userId);

//    public ProductOrder createOrderByProductId(List<Long> productId,
//                                             //  List<Integer> quantity,
//                                               OrderRequest orderRequest);

//    public List<ProductOrder> getOrdersByStatus(String status);

//    public List<ProductOrder> getOrdersByStatusId(Integer statusId);

//    API cho phép tạo đơn cho khách
//    public void saveOrderByStaff(Long userId,
//                                 ProductOrder productOrder,
//                                 OrderRequest orderRequest) throws Exception;

//    public ProductOrder createOrderByProductIdByStaff(Long userId,
//                                                      List<Long> productId,
//                                            //   List<Integer> quantity,
//                                               OrderRequest orderRequest);

//    public void saveOrder(Long cartid, OrderRequest orderRequest) throws Exception;

//    //Hàm cập nhật status của order tổng
//    public ProductOrder updateOrderStatus(Long id, String status);

//    public ProductOrder getOrdersByOrderId(String orderId);

//    public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize);

//    public List<VaccinationHistoryResponse> getCustomerVaccinationHistory(Long customerId);

//    public ProductOrder getOrderById(Long orderId);

    //CUSTOMER xem đơn hàng đã đặt cho
    public List<ProductOrder> getOrdersByUser(Long userId);

    //STAFF xem tất các đơn hàng đã đặt , fillter do FE xử lí
    public List<ProductOrder> getAllOrders();

    //Dashboard
    public List<VaccineOrderStats> getTopVaccines(int month, int year);

    //Dashboard
    public List<VaccineOrderStats> getLeastOrderedVaccines(int month, int year);

    //STAFF xem order = orderid
    public ProductOrder getOrderByOrderId(String orderId);

    //CUSTOMER , STAFF xem lịch sử tiêm chủng của 1 trẻ
    public List<VaccinationHistoryResponse> getChildVaccinationHistory(Long childId);

    //CUSTOMER , STAFF
    public List<UpcomingVaccinationResponse> getUpcomingVaccinations(Long childId);

    //CUSTOMER,STAFF
    public List<UpcomingVaccinationResponse> getUpcomingVaccinationsForAllChildren(Long parentId);

    //STAFF cập nhật tình trạng từng vaccine
    public void updateOrderDetailStatus(Long orderDetailId,
                                        OrderDetailStatus newStatus);

    //STAFF cập nhật ngày tiêm cho vaccine
    public OrderDetail updateVaccinationDate(Long orderDetailId,
                                             LocalDateTime vaccinationDate);

    //Ko xài
    public List<OrderDetailResponse> getUpcomingSchedules(LocalDate date,
                                                          OrderDetailStatus status);

    //Xem toàn bộ lịch tiêm theo tuần cho STAFF , ko nhập => tuần htai , nhập tuần theo cái nhập
    public List<OrderDetailResponse> getWeeklySchedule(LocalDate startDate);

    //Xem lịch tiêm trong ngày
    public List<OrderDetailResponse> getUpcomingSchedulesWithoutStatus(LocalDate date);

    //Gợi ý vaccine CUSTOMER
    public List<ProductSuggestionResponse> suggestVaccinesForChild(Long childId);

    //Gợi ý vaccine cho STAFF
    public List<ProductSuggestionResponse> suggestVaccinesByStaff(Long childId);

    //Hủy đơn CUSTOMER
    public void cancelOrderByCustomer(String orderId,
                                      Long userId,
                                      String reason) throws AccessDeniedException;

    //Hủy đơn STAFF
    public void cancelOrderByStaff(String orderId, String reason);

    //Xem lịch tiêm sắp tới cho STAFF
    public List<OrderDetailResponse> getUpcomingSchedulesForStaff(LocalDateTime fromDate,
                                                                  OrderDetailStatus status);

    //Xem lịch tiêm sắp tới cho CUSTOMER
    public List<OrderDetailResponse> getUpcomingSchedulesForParent(Long parentId,
                                                                   LocalDateTime fromDate,
                                                                   OrderDetailStatus status);

    //Method đặt vaccine cho ph
    public ProductOrder createOrderByChildProductMap(Map<Long,
            List<Long>> childProductMap, OrderRequest orderRequest);

    //Method đặt vaccine cho staff
    public ProductOrder createOrderByStaff(Long parentId,
                                           Map<Long, List<Long>> childProductMap,
                                           OrderRequest orderRequest);




    }
