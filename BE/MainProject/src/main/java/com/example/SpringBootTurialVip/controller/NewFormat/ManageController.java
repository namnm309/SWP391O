package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.*;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.PermissionResponse;
import com.example.SpringBootTurialVip.dto.response.RoleResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.service.*;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private StaffService staffService;

    @Autowired
    private UserService userService;

    //Tạo đối tượng mới
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Gán quyền hệ thống cho đối tượng(admin)",
            description = "Ví dụ : tạo ra quyền UPDATE_POST cho đối tượng STAFF_1 ," +
                    "Đối tượng có quyền UPDATE_POST sẽ có quyền UPDATE bài post ," +
                    "!!!Lưu ý:  phải tạo quyền hệ thống(permission) trước rồi mới tạo quyền này cho đối tượng ví dụ STAFF đc")
    @PostMapping("/createRole")
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    //API Xem  các đối tượng
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "API xem quyền các đối tượng trong hệ thống(admin)",
            description = "Ví dụ : đối tượng USER có quyền UPDATE_CHILD ")
    @GetMapping("/roles")
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    //API xóa đối tượng
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "API xóa 1 role(admin) ")
    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable String role) {
        roleService.delete(role);
        return ApiResponse.<Void>builder().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "APi xóa permission cho 1 đối tượng(admin)")
    @DeleteMapping("/roles/{roleName}/permissions/{permissionName}")
    public ResponseEntity<ApiResponse<String>> removePermissionFromRole(
            @PathVariable String roleName,
            @PathVariable String permissionName) {

        roleService.removePermissionFromRole(roleName, permissionName);

        return ResponseEntity.ok(new ApiResponse<>(1000, "Permission removed successfully from role", null));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Danh sách nhân viên", description = "Lấy danh sách tất cả nhân viên trong hệ thống")
    @GetMapping("/staff-list")
    public ResponseEntity<ApiResponse<List<User>>> getStaffList() {
        return ResponseEntity.ok(new ApiResponse<>(1000, "Success", adminDashboardService.getStaffList()));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Cập nhật thông tin nhân viên", description = "Cập nhật thông tin của nhân viên dựa trên ID")
    @PutMapping("/update-staff/{id}")
    public ResponseEntity<ApiResponse<String>> updateStaff(@PathVariable Long id, @RequestBody StaffUpdateRequest staffRequest) {
        adminDashboardService.updateStaff(id, staffRequest);
        return ResponseEntity.ok(new ApiResponse<>(1000, "Staff updated successfully", null));
    }

    //API tạo quyền quản lí
    @PreAuthorize("hasAnyRole('ADMIN')")
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

    //API xem tất cả quyền quản lí
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Xem các quyền đã tạo")
    @GetMapping("/getAll")
    ApiResponse<List<PermissionResponse>> getAllPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    //API: Xem danh sách tất cả customer
    @Operation(summary = "Xem danh sách tất cả khách hàng(staff,admin)")
    @GetMapping("/parents")
    public ResponseEntity<List<UserResponse>> getAllParents() {
        return ResponseEntity.ok(staffService.getAllParents());
    }

    //API: Xem danh sách tất cả trẻ

    @Operation(summary = "Xem danh sách tất cả trẻ em(staff,admin)")
    @GetMapping("/children")
    public ResponseEntity<List<ChildResponse>> getAllChildren() {
        return ResponseEntity.ok(staffService.getAllChildren());
    }

    //API: Tạo child cho 1 customer theo
    @Operation(summary = "Tạo 1 child cho 1 khách hàng = cách gán parentid của trẻ đc tạo = id của khách")
    @PostMapping("/children/create/{parentId}")
    public ResponseEntity<ChildResponse> createChildForParent(
            @PathVariable("parentId") Long parentId,
            @RequestBody ChildCreationRequest request) {
        return ResponseEntity.ok(staffService.createChildForParent(parentId, request));
    }

    //API Đăng ký  tài khoản staff
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "API tạo tài khoản staff(admin)")
    @PostMapping("/createStaff")
    public ResponseEntity<ApiResponse<UserResponse>> createStaff(@RequestBody @Valid UserCreationRequest request) {
        User user = userService.createStaff(request);

        // Chuyển đổi User -> UserResponse
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setParentid(user.getParentid());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setBod(user.getBod());
        userResponse.setGender(user.getGender());
        userResponse.setFullname(user.getFullname());

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(0, "User created successfully", userResponse);
        return ResponseEntity.ok(apiResponse);
    }

    //Active or Unactive staff ? => edit staff ( nhưng chỉ truyền vào 1 param )



}
