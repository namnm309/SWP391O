package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.dto.request.StaffUpdateRequest;
import com.example.SpringBootTurialVip.dto.response.RevenueResponse;
import com.example.SpringBootTurialVip.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AdminDashboardService {

    // Lấy số lượng đơn vaccine trung bình mỗi ngày
    Double getAverageDailyOrders();

    // Lấy loại vaccine được tiêm nhiều nhất trong tháng hiện tại
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
}
