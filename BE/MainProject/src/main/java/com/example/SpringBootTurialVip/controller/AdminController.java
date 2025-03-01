package com.example.SpringBootTurialVip.controller;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.request.RoleRequest;
import com.example.SpringBootTurialVip.dto.response.RoleResponse;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.service.OrderService;
import com.example.SpringBootTurialVip.service.serviceimpl.RoleServiceImpl;
import com.example.SpringBootTurialVip.service.serviceimpl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name="[ADMIN API]",description = "(Cần authen) Các api chỉ dành riêng dành cho admin")
@PreAuthorize("hasRole('ADMIN')") // chỉ có admin
public class AdminController {
    @Autowired
    private RoleServiceImpl roleServiceImpl;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private OrderService orderService;


    //Tạo quyền mới
    @Operation(summary = "Gán quyền hệ thống cho đối tượng",
            description = "Ví dụ : tạo ra quyền UPDATE_POST cho đối tượng STAFF_1 ," +
                    "Đối tượng có quyền UPDATE_POST sẽ có quyền UPDATE bài post ," +
                    "!!!Lưu ý:  phải tạo quyền hệ thống(permission) trước rồi mới tạo quyền này cho đối tượng ví dụ STAFF đc")
    @PostMapping("/createRole")
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleServiceImpl.create(request))
                .build();
    }

    //API Xem quyền của các đối tượng
    @Operation(summary = "API xem quyền các đối tượng trong hệ thống",
    description = "Ví dụ : đối tượng USER có quyền UPDATE_CHILD ")
    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleServiceImpl.getAll())
                .build();
    }

    //API xóa quyền
    @Operation(summary = "API xóa 1 role ")
    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable String role) {
        roleServiceImpl.delete(role);
        return ApiResponse.<Void>builder().build();
    }

    //API lấy danh sách user
    @Operation(summary = "APi lấy danh sách user")
    @GetMapping("/getUser")
    List<User> getUsers() {
        //Để get thông tin hiện tại đang đc authenticated , chứa thông tin user đang log in hiện tại
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        //In ra thông tin trong console
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority ->
                log.info(grantedAuthority.getAuthority()));

        return userServiceImpl.getUsers();
    }

    @Operation(summary = "APi xóa permission cho 1 đối tượng")
    @DeleteMapping("/roles/{roleName}/permissions/{permissionName}")
    public ResponseEntity<ApiResponse<String>> removePermissionFromRole(
            @PathVariable String roleName,
            @PathVariable String permissionName) {

        roleServiceImpl.removePermissionFromRole(roleName, permissionName);

        return ResponseEntity.ok(new ApiResponse<>(1000, "Permission removed successfully from role", null));
    }

    //=======================================================ADMIN DASHBOARD==================================================================
    //API lấy số đơn vaccine trung bình 1 ngày
//    @GetMapping("/admin/dashboard/average-orders-per-day")
//    public ResponseEntity<Double> getAverageOrdersPerDay() {
//        double avgOrders = orderService.getAverageOrdersPerDay();
//        return ResponseEntity.ok(avgOrders);
//    }

    //API xem vaccine được chích nhiều nhất
    @Operation(summary = "API Xem số đơn vaccine trung bình 1 ngày ")
    @GetMapping
    ApiResponse<List<?>> GetApi() {
        return ApiResponse.<List<?>>builder()
                .result(roleServiceImpl.getAll())
                .build();
    }

    //API xem vaccine được chích nhiều nhất tháng này
    @Operation(summary = "API Xem số đơn vaccine trung bình 1 ngày ")
    @GetMapping
    ApiResponse<List<?>> MostVaccineThisMonth() {
        return ApiResponse.<List<?>>builder()
                .result(roleServiceImpl.getAll())
                .build();
    }
    //API xem tổng doanh thu theo tuần , tháng , năm ( dựa theo tbl_product order khi đơn hàng ở status thành công)
    @Operation(summary = "API Xem tổng doanh thu theo tuần , tháng , năm ")
    @GetMapping
    ApiResponse<List<?>> total() {
        return ApiResponse.<List<?>>builder()
                .result(roleServiceImpl.getAll())
                .build();
    }

    //API xem độ tuổi của trẻ được tiêm nhiều nhất (Dữ liệu cho biểu đồ)
    @Operation(summary = "API Xem độ tuổi của trẻ được tiêm nhiều nhất (Dữ liệu cho biểu đồ")
    @GetMapping
    ApiResponse<List<?>> child() {
        return ApiResponse.<List<?>>builder()
                .result(roleServiceImpl.getAll())
                .build();
    }

    //API xem tỷ lệ tiêm chủng theo từng loại vaccine

    //API xem đánh giá & phản hồi khách hàng,nắm bắt mức độ hài lòng của khách hàng về dịch vụ tiêm chủng. (Thêm bảng tbl_feedback)

    //APi xem danh sách sản phẩm

    //APi xem danh sách category

    //API xem danh sách staff

    //API thêm staff

    //API edit staff ( active or unactive tk , delete ? )
        //API gửi thông báo đến staff

}
