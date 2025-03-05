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

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name="[Financal(admin,staff)]",description = "")
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
     * @param month The month to filter (1-12)
     * @param year The year to filter
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
}
