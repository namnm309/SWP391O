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

//    public void saveOrder(Long cartid, OrderRequest orderRequest) throws Exception;

    public List<ProductOrder> getOrdersByUser(Long userId);

    public ProductOrder updateOrderStatus(Long id, String status);

    //Admin dashboard : Số đơn vaccine trung bình 1 ngày
    //public double getAverageOrdersPerDay() ;

    public List<ProductOrder> getAllOrders();

    public ProductOrder getOrdersByOrderId(String orderId);

    public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize);

    public List<VaccineOrderStats> getTopVaccines(int month, int year);

    public List<VaccineOrderStats> getLeastOrderedVaccines(int month, int year);

    public ProductOrder getOrderById(Long orderId);

   // public void saveOrderByProductId(Long productId, OrderRequest orderRequest, Long userId);

//    public ProductOrder createOrderByProductId(List<Long> productId,
//                                             //  List<Integer> quantity,
//                                               OrderRequest orderRequest);

//    public List<ProductOrder> getOrdersByStatus(String status);

    //public List<ProductOrder> getOrdersByStatusId(Integer statusId);

    //API cho phép tạo đơn cho khách
//    public void saveOrderByStaff(Long userId,
//                                 ProductOrder productOrder,
//                                 OrderRequest orderRequest) throws Exception;

//    public ProductOrder createOrderByProductIdByStaff(Long userId,
//                                                      List<Long> productId,
//                                            //   List<Integer> quantity,
//                                               OrderRequest orderRequest);

    public ProductOrder getOrderByOrderId(String orderId);

    public List<VaccinationHistoryResponse> getChildVaccinationHistory(Long childId);

    public List<UpcomingVaccinationResponse> getUpcomingVaccinations(Long childId);

    public List<UpcomingVaccinationResponse> getUpcomingVaccinationsForAllChildren(Long parentId);

//    public List<VaccinationHistoryResponse> getCustomerVaccinationHistory(Long customerId);

    public void updateOrderDetailStatus(Long orderDetailId, OrderDetailStatus newStatus);

    public OrderDetail updateVaccinationDate(Long orderDetailId, LocalDateTime vaccinationDate);

    public List<OrderDetailResponse> getUpcomingSchedules(LocalDate date, OrderDetailStatus status);

    public List<OrderDetailResponse> getWeeklySchedule(LocalDate startDate);

    public List<OrderDetailResponse> getUpcomingSchedulesWithoutStatus(LocalDate date);

    //Gợi ý vaccine
    public List<ProductSuggestionResponse> suggestVaccinesForChild(Long childId);

    public List<ProductSuggestionResponse> suggestVaccinesByStaff(Long childId);

    public void cancelOrderByCustomer(String orderId, Long userId) throws AccessDeniedException;

    public void cancelOrderByStaff(String orderId, String reason);

    public List<OrderDetailResponse> getUpcomingSchedulesForStaff(LocalDateTime fromDate, OrderDetailStatus status);

    public List<OrderDetailResponse> getUpcomingSchedulesForParent(Long parentId, LocalDateTime fromDate, OrderDetailStatus status);

    public ProductOrder createOrderByChildProductMap(Map<Long, List<Long>> childProductMap, OrderRequest orderRequest);

    ProductOrder createOrderByStaff(Map<Long, List<Long>> childProductMap, OrderRequest orderRequest);




    }
