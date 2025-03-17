package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.dto.request.StaffUpdateRequest;
import com.example.SpringBootTurialVip.dto.response.RevenueResponse;
import com.example.SpringBootTurialVip.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public interface AdminDashboardService {

    // Lấy số lượng đơn vaccine trung bình mỗi ngày
    Double getAverageDailyOrders();

    //Lấy loại vaccine được tiêm nhiều nhất trong tháng hiện tại
    String getTopVaccineOfMonth();

    // Lấy tổng doanh thu theo tuần, tháng hoặc năm
    // @param type Loại doanh thu cần lấy ("week", "month", "year")
    public RevenueResponse getRevenue(String startDate, String endDate);

    // Tìm độ tuổi của trẻ được tiêm vaccine nhiều nhất
    Integer getMostVaccinatedAge();

    // Lấy danh sách tất cả các nhân viên (staff)
    List<User> getStaffList();

    // Thêm một nhân viên mới vào hệ thống
//    void addStaff(User staff);

    // Cập nhật thông tin của một nhân viên cụ thể
    void updateStaff(Long id, StaffUpdateRequest staff);

    // Gửi thông báo đến toàn bộ nhân viên trong hệ thống
    void sendNotificationToStaff(String message);
//
//    long getNewCustomersLast7Days();
//    long getNewCustomersLast30Days();
//    long getNewCustomersLast90Days();
//    long getTotalCustomers();
//    Map<String, Long> getCustomerGrowthStats();
//
//
//
//    Map<String, Object> getTopVaccineLast7Days();
//    Map<String, Object> getTopVaccineLast30Days();
//    Map<String, Object> getTopVaccineLast90Days();
//    Map<String, Map<String, Object>> getTopVaccinesStats();
//
//
//
//    Double getRevenueLast7Days();
//    Double getRevenueLast30Days();
//    Double getRevenueLast90Days();
//    Map<String, Double> getRevenueStats();
//
//
//
//    Map<String, Object> getMostVaccinatedAgeLast7Days();
//    Map<String, Object> getMostVaccinatedAgeLast30Days();
//    Map<String, Object> getMostVaccinatedAgeLast90Days();
//    Map<String, Map<String, Object>> getMostVaccinatedAgeStats();
//
//
//
//
//    Map<String, Object> getLeastOrderedVaccineLast7Days();
//    Map<String, Object> getLeastOrderedVaccineLast30Days();
//    Map<String, Object> getLeastOrderedVaccineLast90Days();
//    Map<String, Map<String, Object>> getLeastOrderedVaccinesStats();


    Map<LocalDate, Long> getDailyNewCustomers(int days);
    Map<LocalDate, Double> getDailyRevenue(int days);
    Map<LocalDate, Map<String, Object>> getDailyTopVaccine(int days);
    Map<LocalDate, Map<String, Object>> getDailyLeastOrderedVaccine(int days);
    Map<LocalDate, Map<String, Object>> getDailyMostVaccinatedAge(int days);


}