package com.example.SpringBootTurialVip.controller;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.request.RoleRequest;
import com.example.SpringBootTurialVip.dto.response.RoleResponse;
import com.example.SpringBootTurialVip.entity.Role;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.service.serviceimpl.RoleService;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name="[ADMIN API]",description = "(Cần authen) Các api chỉ dành riêng dành cho admin")
public class AdminController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;


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
    @Operation(summary = "Chưa hoàn thành !!!")
    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable String role) {
        roleService.delete(role);
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

        return userService.getUsers();
    }
}
