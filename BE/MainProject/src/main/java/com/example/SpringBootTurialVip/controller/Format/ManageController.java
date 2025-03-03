package com.example.SpringBootTurialVip.controller.Format;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.request.PermissionRequest;
import com.example.SpringBootTurialVip.dto.request.RoleRequest;
import com.example.SpringBootTurialVip.dto.request.StaffUpdateRequest;
import com.example.SpringBootTurialVip.dto.response.PermissionResponse;
import com.example.SpringBootTurialVip.dto.response.RoleResponse;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.service.AdminDashboardService;
import com.example.SpringBootTurialVip.service.AuthenticationService;
import com.example.SpringBootTurialVip.service.PermissionService;
import com.example.SpringBootTurialVip.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manage")
@RequiredArgsConstructor
@Tag(name="[Manage]",description = "")
public class ManageController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private AdminDashboardService adminDashboardService;

    //Tạo quyền mới
    @Operation(summary = "Gán quyền hệ thống cho đối tượng",
            description = "Ví dụ : tạo ra quyền UPDATE_POST cho đối tượng STAFF_1 ," +
                    "Đối tượng có quyền UPDATE_POST sẽ có quyền UPDATE bài post ," +
                    "!!!Lưu ý:  phải tạo quyền hệ thống(permission) trước rồi mới tạo quyền này cho đối tượng ví dụ STAFF đc")
    @PostMapping("/createRole")
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    //API Xem quyền của các đối tượng
    @Operation(summary = "API xem quyền các đối tượng trong hệ thống",
            description = "Ví dụ : đối tượng USER có quyền UPDATE_CHILD ")
    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    //API xóa quyền
    @Operation(summary = "API xóa 1 role ")
    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable String role) {
        roleService.delete(role);
        return ApiResponse.<Void>builder().build();
    }

    @Operation(summary = "APi xóa permission cho 1 đối tượng")
    @DeleteMapping("/roles/{roleName}/permissions/{permissionName}")
    public ResponseEntity<ApiResponse<String>> removePermissionFromRole(
            @PathVariable String roleName,
            @PathVariable String permissionName) {

        roleService.removePermissionFromRole(roleName, permissionName);

        return ResponseEntity.ok(new ApiResponse<>(1000, "Permission removed successfully from role", null));
    }

    @Operation(summary = "Danh sách nhân viên", description = "Lấy danh sách tất cả nhân viên trong hệ thống")
    @GetMapping("/staff-list")
    public ResponseEntity<ApiResponse<List<User>>> getStaffList() {
        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getStaffList()));
    }

    @Operation(summary = "Cập nhật thông tin nhân viên", description = "Cập nhật thông tin của nhân viên dựa trên ID")
    @PutMapping("/update-staff/{id}")
    public ResponseEntity<ApiResponse<String>> updateStaff(@PathVariable Long id, @RequestBody StaffUpdateRequest staffRequest) {
        adminDashboardService.updateStaff(id, staffRequest);
        return ResponseEntity.ok(new ApiResponse<>(1000, "Staff updated successfully", null));
    }

    //API tạo quyền quản lí
    @PostMapping("/create")
    @Operation(summary = "API tạo quyền hệ thống ",description = "Ví dụ : " +
            "Quyền UPDATE_POST cho phép update bài viết " +
            "Lưu ý:  phải tạo quyền hệ thống trước rồi mới tạo quyền này cho đối tượng ví dụ STAFF đc" +
            "FORM yêu cầu : ví dụ : ADD_PRODUCT")
    ApiResponse<PermissionResponse> create(@RequestBody
                                           //@Valid //tuân thủ request
                                           PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

}
