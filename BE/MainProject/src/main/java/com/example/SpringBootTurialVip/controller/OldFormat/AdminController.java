//package com.example.SpringBootTurialVip.controller.OldFormat;
//
//import com.example.SpringBootTurialVip.dto.request.ApiResponse;
//import com.example.SpringBootTurialVip.dto.request.RoleRequest;
//import com.example.SpringBootTurialVip.dto.request.StaffUpdateRequest;
//import com.example.SpringBootTurialVip.dto.response.RevenueResponse;
//import com.example.SpringBootTurialVip.dto.response.RoleResponse;
//import com.example.SpringBootTurialVip.entity.Feedback;
//import com.example.SpringBootTurialVip.entity.User;
//import com.example.SpringBootTurialVip.service.AdminDashboardService;
//import com.example.SpringBootTurialVip.service.FeedbackService;
//import com.example.SpringBootTurialVip.service.NotificationService;
//import com.example.SpringBootTurialVip.service.OrderService;
//import com.example.SpringBootTurialVip.service.serviceimpl.RoleServiceImpl;
//import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
////@RestController
//@RequestMapping("/admin")
//@RequiredArgsConstructor
//@Slf4j
//@Tag(name="[ADMIN API]",description = "(Cần authen) Các api chỉ dành riêng dành cho admin")
//@PreAuthorize("hasRole('ADMIN')") // chỉ có admin
//public class AdminController {
//    @Autowired
//    private RoleServiceImpl roleServiceImpl;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private FeedbackService feedbackService;
//
//    @Autowired
//    private NotificationService notificationService;
//
//    @Autowired
//    private final AdminDashboardService adminDashboardService;
//
//    //Tạo quyền mới
//    @Operation(summary = "Gán quyền hệ thống cho đối tượng",
//            description = "Ví dụ : tạo ra quyền UPDATE_POST cho đối tượng STAFF_1 ," +
//                    "Đối tượng có quyền UPDATE_POST sẽ có quyền UPDATE bài post ," +
//                    "!!!Lưu ý:  phải tạo quyền hệ thống(permission) trước rồi mới tạo quyền này cho đối tượng ví dụ STAFF đc")
//    @PostMapping("/createRole")
//    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
//        return ApiResponse.<RoleResponse>builder()
//                .result(roleServiceImpl.create(request))
//                .build();
//    }
//
//    //API Xem quyền của các đối tượng
//    @Operation(summary = "API xem quyền các đối tượng trong hệ thống",
//    description = "Ví dụ : đối tượng USER có quyền UPDATE_CHILD ")
//    @GetMapping
//    ApiResponse<List<RoleResponse>> getAll() {
//        return ApiResponse.<List<RoleResponse>>builder()
//                .result(roleServiceImpl.getAll())
//                .build();
//    }
//
//    //API xóa quyền
//    @Operation(summary = "API xóa 1 role ")
//    @DeleteMapping("/{role}")
//    ApiResponse<Void> delete(@PathVariable String role) {
//        roleServiceImpl.delete(role);
//        return ApiResponse.<Void>builder().build();
//    }
//
//    //API lấy danh sách user
//    @Operation(summary = "APi lấy danh sách user")
//    @GetMapping("/getUser")
//    List<User> getUsers() {
//        //Để get thông tin hiện tại đang đc authenticated , chứa thông tin user đang log in hiện tại
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        //In ra thông tin trong console
//        log.info("Username: {}", authentication.getName());
//        authentication.getAuthorities().forEach(grantedAuthority ->
//                log.info(grantedAuthority.getAuthority()));
//
//        return userService.getUsers();
//    }
//
//    @Operation(summary = "APi xóa permission cho 1 đối tượng")
//    @DeleteMapping("/roles/{roleName}/permissions/{permissionName}")
//    public ResponseEntity<ApiResponse<String>> removePermissionFromRole(
//            @PathVariable String roleName,
//            @PathVariable String permissionName) {
//
//        roleServiceImpl.removePermissionFromRole(roleName, permissionName);
//
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Permission removed successfully from role", null));
//    }
//
//    //=======================================================ADMIN DASHBOARD==================================================================
//    @Operation(
//            summary = "API lấy danh sách đánh giá theo số sao",
//            description = "Cho phép admin lọc và xem danh sách đánh giá theo số sao từ 1 đến 5."
//    )
//    @GetMapping("/feedback/rating/{stars}")
//    public ResponseEntity<List<Feedback>> getFeedbackByRating(@PathVariable int stars) {
//        return ResponseEntity.ok(feedbackService.getFeedbackByRating(stars));
//    }
//
//    @Operation(
//            summary = "API lấy số sao trung bình",
//            description = "Cho phép admin xem số sao trung bình của tất cả đánh giá trên hệ thống."
//    )
//    @GetMapping("/feedback/average-rating")
//    public ResponseEntity<Double> getAverageRating() {
//        return ResponseEntity.ok(feedbackService.getAverageRating());
//    }
//
//    //=======================================================Gửi thông báo đến all staff==================================================================
//
//    @Operation(summary = "API gửi thông báo đến tất cả staff", description = "Cho phép admin gửi thông báo đến tất cả nhân viên có role STAFF.")
//    @PostMapping("/notifications/staff")
//    public ResponseEntity<String> sendNotificationToAllStaff(@RequestParam String message) {
//        notificationService.sendNotificationToAllStaff(message);
//        return ResponseEntity.ok("Notification sent to all staff successfully");
//    }
//    //=======================================================================================================================================================
//
//    @Operation(summary = "Lấy số đơn vaccine trung bình mỗi ngày", description = "Tính toán số lượng đơn vaccine trung bình theo ngày")
//    @GetMapping("/avg-daily-orders")
//    public ResponseEntity<ApiResponse<Double>> getAvgDailyOrders() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getAverageDailyOrders()));
//    }
//
//    @Operation(summary = "Vaccine được tiêm nhiều nhất trong tháng", description = "Lấy thông tin loại vaccine phổ biến nhất trong tháng hiện tại")
//    @GetMapping("/top-vaccine")
//    public ResponseEntity<ApiResponse<String>> getTopVaccineOfMonth() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getTopVaccineOfMonth()));
//    }
//
//    @Operation(summary = "Tổng doanh thu theo khoảng thời gian", description = "Lấy tổng doanh thu trong khoảng thời gian cụ thể")
//    @GetMapping("/revenue")
//    public ResponseEntity<ApiResponse<RevenueResponse>> getRevenue(
//            @Parameter(description = "Ngày bắt đầu (YYYY-MM-DD)") @RequestParam(value = "startDate") String startDate,
//            @Parameter(description = "Ngày kết thúc (YYYY-MM-DD)") @RequestParam(value = "endDate") String endDate) {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getRevenue(startDate, endDate)));
//    }
//
//    @Operation(summary = "Độ tuổi của trẻ được tiêm nhiều nhất", description = "Lấy độ tuổi phổ biến nhất của trẻ đã tiêm vaccine")
//    @GetMapping("/most-vaccinated-age")
//    public ResponseEntity<ApiResponse<Integer>> getMostVaccinatedAge() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getMostVaccinatedAge()));
//    }
//
//    @Operation(summary = "Danh sách nhân viên", description = "Lấy danh sách tất cả nhân viên trong hệ thống")
//    @GetMapping("/staff-list")
//    public ResponseEntity<ApiResponse<List<User>>> getStaffList() {
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getStaffList()));
//    }
//
////    @Operation(summary = "Thêm nhân viên mới", description = "Thêm một nhân viên mới vào hệ thống với thông tin cần thiết")
////    @PostMapping("/add-staff")
////    public ResponseEntity<ApiResponse<String>> addStaff(@RequestBody User staffRequest) {
////        adminDashboardService.addStaff(staffRequest);
////        return ResponseEntity.ok(new ApiResponse<>(1000, "Staff added successfully", null));
////    }
//
//    @Operation(summary = "Cập nhật thông tin nhân viên", description = "Cập nhật thông tin của nhân viên dựa trên ID")
//    @PutMapping("/update-staff/{id}")
//    public ResponseEntity<ApiResponse<String>> updateStaff(@PathVariable Long id, @RequestBody StaffUpdateRequest staffRequest) {
//        adminDashboardService.updateStaff(id, staffRequest);
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Staff updated successfully", null));
//    }
//
//    @Operation(summary = "Gửi thông báo đến toàn bộ nhân viên", description = "Gửi một thông báo đến tất cả nhân viên trong hệ thống")
//    @PostMapping("/send-notification")
//    public ResponseEntity<ApiResponse<String>> sendNotification(@RequestParam String message) {
//        adminDashboardService.sendNotificationToStaff(message);
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Notification sent successfully", null));
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
//                //API lấy số đơn vaccine trung bình 1 ngày
//            //    @GetMapping("/admin/dashboard/average-orders-per-day")
//            //    public ResponseEntity<Double> getAverageOrdersPerDay() {
//            //        double avgOrders = orderService.getAverageOrdersPerDay();
//            //        return ResponseEntity.ok(avgOrders);
//            //    }
//
//                //API xem vaccine được chích nhiều nhất
//                //API xem tổng doanh thu theo tuần , tháng , năm ( dựa theo tbl_product order khi đơn hàng ở status thành công)
//                //API xem độ tuổi của trẻ được tiêm nhiều nhất (Dữ liệu cho biểu đồ)
//                //API xem tỷ lệ tiêm chủng theo từng loại vaccine
//                //API xem đánh giá & phản hồi khách hàng,nắm bắt mức độ hài lòng của khách hàng về dịch vụ tiêm chủng. (Thêm bảng tbl_feedback)
//                //APi xem danh sách sản phẩm
//                //APi xem danh sách category
//                //API xem danh sách staff
//                //API thêm staff
//                //API edit staff ( active or unactive tk , delete ? )
//                    //API gửi thông báo đến staff
//
//}
