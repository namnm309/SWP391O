package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.response.RevenueResponse;
import com.example.SpringBootTurialVip.repository.VaccineOrderStats;
import com.example.SpringBootTurialVip.service.AdminDashboardService;
import com.example.SpringBootTurialVip.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name="[Financal(admin,staff)]",description = "")
@PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
public class DashboardController {


    @Autowired
    private AdminDashboardService adminDashboardService;

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
    @Operation(summary = "Lấy số đơn vaccine trung bình mỗi ngày", description = "Tính toán số lượng đơn vaccine trung bình theo ngày")
    @GetMapping("/avg-daily-orders")
    public ResponseEntity<ApiResponse<Double>> getAvgDailyOrders() {
        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getAverageDailyOrders()));
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
    @Operation(summary = "Vaccine được tiêm nhiều nhất trong tháng", description = "Lấy thông tin loại vaccine phổ biến nhất trong tháng hiện tại")
    @GetMapping("/top-vaccine")
    public ResponseEntity<ApiResponse<String>> getTopVaccineOfMonth() {
        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getTopVaccineOfMonth()));
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
    @Operation(summary = "Tổng doanh thu theo khoảng thời gian", description = "Lấy tổng doanh thu trong khoảng thời gian cụ thể")
    @GetMapping("/revenue")
    public ResponseEntity<ApiResponse<RevenueResponse>> getRevenue(
            @Parameter(description = "Ngày bắt đầu (YYYY-MM-DD)") @RequestParam(value = "startDate") String startDate,
            @Parameter(description = "Ngày kết thúc (YYYY-MM-DD)") @RequestParam(value = "endDate") String endDate) {
        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getRevenue(startDate, endDate)));
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
    @Operation(summary = "Độ tuổi của trẻ được tiêm nhiều nhất", description = "Lấy độ tuổi phổ biến nhất của trẻ đã tiêm vaccine")
    @GetMapping("/most-vaccinated-age")
    public ResponseEntity<ApiResponse<Integer>> getMostVaccinatedAge() {
        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getMostVaccinatedAge()));
    }

    /**
     * Get top 5 most ordered vaccines for a specific month and year.
     *
     * @param month The month to filter (1-12)
     * @param year  The year to filter
     * @return List of top 5 vaccines with order counts
     */
    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
    @Operation(summary = "Lấy 5 loại vaccine đc tiêm nhiều nhất trong tháng và năm chỉ định",
            description = "")
    @GetMapping("/top-vaccines")
    public ResponseEntity<List<VaccineOrderStats>> getTopVaccines(
            @RequestParam @Parameter(description = "Month (1-12)", example = "2") int month,
            @RequestParam @Parameter(description = "Year", example = "2025") int year) {
        List<VaccineOrderStats> topVaccines = orderService.getTopVaccines(month, year);
        return ResponseEntity.ok(topVaccines);
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
    @Operation(summary = "Lấy 5 loại vaccine đc tiêm ít nhất trong tháng và năm chỉ định",
            description = "")
    @GetMapping("/least-ordered-vaccines")
    public ResponseEntity<List<VaccineOrderStats>> getLeastOrderedVaccines(
            @RequestParam @Parameter(description = "Month (1-12)", example = "2") int month,
            @RequestParam @Parameter(description = "Year", example = "2025") int year) {
        List<VaccineOrderStats> leastOrderedVaccines = orderService.getLeastOrderedVaccines(month, year);
        return ResponseEntity.ok(leastOrderedVaccines);
    }


//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Lấy số khách hàng mới trong 7 ngày", description = "Thống kê số khách hàng mới trong 7 ngày qua")
//    @GetMapping("/new-customers/7-days")
//    public ResponseEntity<ApiResponse<Long>> getNewCustomersLast7Days() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getNewCustomersLast7Days()));
//    }
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Lấy số khách hàng mới trong 30 ngày", description = "Thống kê số khách hàng mới trong 30 ngày qua")
//    @GetMapping("/new-customers/30-days")
//    public ResponseEntity<ApiResponse<Long>> getNewCustomersLast30Days() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getNewCustomersLast30Days()));
//    }
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Lấy số khách hàng mới trong 90 ngày", description = "Thống kê số khách hàng mới trong 90 ngày qua")
//    @GetMapping("/new-customers/90-days")
//    public ResponseEntity<ApiResponse<Long>> getNewCustomersLast90Days() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getNewCustomersLast90Days()));
//    }
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Lấy dữ liệu tăng trưởng khách hàng", description = "Dữ liệu khách hàng mới theo 7, 30, 90 ngày để vẽ biểu đồ")
//    @GetMapping("/customer-growth")
//    public ResponseEntity<ApiResponse<Map<String, Long>>> getCustomerGrowthStats() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getCustomerGrowthStats()));
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Top vaccine được tiêm nhiều nhất trong 7 ngày", description = "Lấy loại vaccine phổ biến nhất trong 7 ngày qua, kèm số lượng mũi tiêm")
//    @GetMapping("/top-vaccine/7-days")
//    public ResponseEntity<ApiResponse<Map<String, Object>>> getTopVaccineLast7Days() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getTopVaccineLast7Days()));
//    }
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Top vaccine được tiêm nhiều nhất trong 30 ngày", description = "Lấy loại vaccine phổ biến nhất trong 30 ngày qua, kèm số lượng mũi tiêm")
//    @GetMapping("/top-vaccine/30-days")
//    public ResponseEntity<ApiResponse<Map<String, Object>>> getTopVaccineLast30Days() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getTopVaccineLast30Days()));
//    }
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Top vaccine được tiêm nhiều nhất trong 90 ngày", description = "Lấy loại vaccine phổ biến nhất trong 90 ngày qua, kèm số lượng mũi tiêm")
//    @GetMapping("/top-vaccine/90-days")
//    public ResponseEntity<ApiResponse<Map<String, Object>>> getTopVaccineLast90Days() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getTopVaccineLast90Days()));
//    }
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Dữ liệu top vaccine cho biểu đồ", description = "Lấy dữ liệu vaccine phổ biến nhất trong 7, 30, 90 ngày để vẽ biểu đồ")
//    @GetMapping("/top-vaccine-stats")
//    public ResponseEntity<ApiResponse<Map<String, Map<String, Object>>>> getTopVaccinesStats() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getTopVaccinesStats()));
//    }
//
//
//
//
//
//
//
//
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Tổng doanh thu trong 7 ngày", description = "Lấy tổng doanh thu trong 7 ngày qua")
//    @GetMapping("/revenue/7-days")
//    public ResponseEntity<ApiResponse<Double>> getRevenueLast7Days() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getRevenueLast7Days()));
//    }
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Tổng doanh thu trong 30 ngày", description = "Lấy tổng doanh thu trong 30 ngày qua")
//    @GetMapping("/revenue/30-days")
//    public ResponseEntity<ApiResponse<Double>> getRevenueLast30Days() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getRevenueLast30Days()));
//    }
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Tổng doanh thu trong 90 ngày", description = "Lấy tổng doanh thu trong 90 ngày qua")
//    @GetMapping("/revenue/90-days")
//    public ResponseEntity<ApiResponse<Double>> getRevenueLast90Days() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getRevenueLast90Days()));
//    }
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Dữ liệu tổng doanh thu cho biểu đồ", description = "Lấy tổng doanh thu trong 7, 30, 90 ngày để vẽ biểu đồ")
//    @GetMapping("/revenue-stats")
//    public ResponseEntity<ApiResponse<Map<String, Double>>> getRevenueStats() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getRevenueStats()));
//    }
//
//
//
//
//
//
//
//
//
//
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Độ tuổi được tiêm nhiều nhất trong 7 ngày", description = "Lấy độ tuổi phổ biến nhất của trẻ đã tiêm vaccine trong 7 ngày qua")
//    @GetMapping("/most-vaccinated-age/7-days")
//    public ResponseEntity<ApiResponse<Map<String, Object>>> getMostVaccinatedAgeLast7Days() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getMostVaccinatedAgeLast7Days()));
//    }
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Độ tuổi được tiêm nhiều nhất trong 30 ngày", description = "Lấy độ tuổi phổ biến nhất của trẻ đã tiêm vaccine trong 30 ngày qua")
//    @GetMapping("/most-vaccinated-age/30-days")
//    public ResponseEntity<ApiResponse<Map<String, Object>>> getMostVaccinatedAgeLast30Days() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getMostVaccinatedAgeLast30Days()));
//    }
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Độ tuổi được tiêm nhiều nhất trong 90 ngày", description = "Lấy độ tuổi phổ biến nhất của trẻ đã tiêm vaccine trong 90 ngày qua")
//    @GetMapping("/most-vaccinated-age/90-days")
//    public ResponseEntity<ApiResponse<Map<String, Object>>> getMostVaccinatedAgeLast90Days() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getMostVaccinatedAgeLast90Days()));
//    }
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Dữ liệu độ tuổi được tiêm nhiều nhất cho biểu đồ", description = "Lấy độ tuổi được tiêm nhiều nhất trong 7, 30, 90 ngày để vẽ biểu đồ")
//    @GetMapping("/most-vaccinated-age-stats")
//    public ResponseEntity<ApiResponse<Map<String, Map<String, Object>>>> getMostVaccinatedAgeStats() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getMostVaccinatedAgeStats()));
//    }
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Vaccine được tiêm ít nhất trong 7 ngày", description = "Lấy loại vaccine ít được tiêm nhất trong 7 ngày qua")
//    @GetMapping("/least-ordered-vaccine/7-days")
//    public ResponseEntity<ApiResponse<Map<String, Object>>> getLeastOrderedVaccineLast7Days() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getLeastOrderedVaccineLast7Days()));
//    }
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Vaccine được tiêm ít nhất trong 30 ngày", description = "Lấy loại vaccine ít được tiêm nhất trong 30 ngày qua")
//    @GetMapping("/least-ordered-vaccine/30-days")
//    public ResponseEntity<ApiResponse<Map<String, Object>>> getLeastOrderedVaccineLast30Days() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getLeastOrderedVaccineLast30Days()));
//    }
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Vaccine được tiêm ít nhất trong 90 ngày", description = "Lấy loại vaccine ít được tiêm nhất trong 90 ngày qua")
//    @GetMapping("/least-ordered-vaccine/90-days")
//    public ResponseEntity<ApiResponse<Map<String, Object>>> getLeastOrderedVaccineLast90Days() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getLeastOrderedVaccineLast90Days()));
//    }
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Dữ liệu vaccine được tiêm ít nhất cho biểu đồ", description = "Lấy dữ liệu vaccine được tiêm ít nhất trong 7, 30, 90 ngày để vẽ biểu đồ")
//    @GetMapping("/least-ordered-vaccine-stats")
//    public ResponseEntity<ApiResponse<Map<String, Map<String, Object>>>> getLeastOrderedVaccinesStats() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getLeastOrderedVaccinesStats()));
//    }
//
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
//    @Operation(summary = "Lấy toàn bộ dữ liệu thống kê", description = "API tổng hợp tất cả dữ liệu từ các API khác để vẽ biểu đồ")
//    @GetMapping("/show-all")
//    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllDashboardData() {
//        Map<String, Object> response = new HashMap<>();
//
//        //  Lấy tổng số khách hàng mới
//        response.put("newCustomers", adminDashboardService.getCustomerGrowthStats());
//
//        //  Lấy top vaccine được tiêm nhiều nhất
//        response.put("topVaccines", adminDashboardService.getTopVaccinesStats());
//
//        //  Lấy vaccine được tiêm ít nhất
//        response.put("leastVaccines", adminDashboardService.getLeastOrderedVaccinesStats());
//
//        //  Lấy tổng doanh thu
//        response.put("revenue", adminDashboardService.getRevenueStats());
//
//        //  Lấy độ tuổi được tiêm nhiều nhất
//        response.put("mostVaccinatedAge", adminDashboardService.getMostVaccinatedAgeStats());
//
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", response));
//    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
    @GetMapping("/day/new-customers")
    @Operation(summary = "Lấy số khách hàng mới trong X ngày", description = "Thống kê số khách hàng mới trong X ngày qua")
    public ResponseEntity<ApiResponse<Map<LocalDate, Long>>> getNewCustomers(@RequestParam int days) {
        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getDailyNewCustomers(days)));
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
    @GetMapping("/day/revenue")
    @Operation(summary = "Tổng doanh thu trong X ngày", description = "Lấy tổng doanh thu trong X ngày qua")
    public ResponseEntity<ApiResponse<Map<LocalDate, Double>>> getRevenue(@RequestParam int days) {
        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getDailyRevenue(days)));
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
    @GetMapping("/day/most-vaccine")
    @Operation(summary = "Top vaccine được tiêm nhiều nhất trong X ngày", description = "Lấy loại vaccine phổ biến nhất trong X ngày qua")
    public ResponseEntity<ApiResponse<Map<LocalDate, Map<String, Object>>>> getTopVaccine(@RequestParam int days) {
        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getDailyTopVaccine(days)));
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
    @GetMapping("/day/least-vaccine")
    @Operation(summary = "Vaccine được tiêm ít nhất trong X ngày", description = "Lấy loại vaccine ít được tiêm nhất trong X ngày qua")
    public ResponseEntity<ApiResponse<Map<LocalDate, Map<String, Object>>>> getLeastOrderedVaccine(@RequestParam int days) {
        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getDailyLeastOrderedVaccine(days)));
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN','TEST')")
    @GetMapping("/day/most-vaccinated-age")
    @Operation(summary = "Độ tuổi được tiêm nhiều nhất trong X ngày", description = "Lấy độ tuổi phổ biến nhất của trẻ đã tiêm vaccine trong X ngày qua")
    public ResponseEntity<ApiResponse<Map<LocalDate, Map<String, Object>>>> getMostVaccinatedAge(@RequestParam int days) {
        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getDailyMostVaccinatedAge(days)));
    }

}