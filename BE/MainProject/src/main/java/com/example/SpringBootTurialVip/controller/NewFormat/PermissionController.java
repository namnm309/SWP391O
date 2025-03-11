package com.example.SpringBootTurialVip.controller.NewFormat;


import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.request.PermissionRequest;
import com.example.SpringBootTurialVip.dto.response.PermissionResponse;
import com.example.SpringBootTurialVip.service.serviceimpl.PermissionServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "[PermissionController dành cho Admin]",description = "(Cần authen) dành cho admin ," +
        " các api quản lí quyền hệ thống ," +
        "Ví dụ : quyền trả lời comment , quyền update post cho staff (CHỈ CHO STAFF)")
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController {
    @Autowired
    PermissionServiceImpl permissionServiceImpl;

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
                .result(permissionServiceImpl.create(request))
                .build();
    }

    //API xem tất cả quyền quản lí
    @Operation(summary = "Xem các quyền đã tạo")
    @GetMapping("/getAll")
    ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionServiceImpl.getAll())
                .build();
    }

    //API xóa quyền quản lí
    @Operation(summary = "Xóa 1 quyền đã tạo",description =
            "Phải xóa đúng tên quyền đã tạo . " +
                    "Ví dụ : tạo quyền UPDATE_POST khi xóa phải truyền vào param UPDATE_POST ")
    @DeleteMapping("/delete/{permission}")
    ApiResponse<Void> delete(@PathVariable String permission) {
        permissionServiceImpl.delete(permission);
        return ApiResponse.<Void>builder().build();
    }
}
