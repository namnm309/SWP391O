package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.*;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.PermissionResponse;
import com.example.SpringBootTurialVip.dto.response.RoleResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.enums.RelativeType;
import com.example.SpringBootTurialVip.service.*;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/manage")
@RequiredArgsConstructor
@Tag(name="[Manage]",description = "")
@PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
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

    @Autowired
    private ObjectMapper objectMapper;

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
    @PreAuthorize("hasAnyRole('STAFF','ROLE_ROLE_STAFF','ADMIN')")
    @Operation(summary = "API tạo 1 child cho khách hàng")
    @PostMapping(value = "/children/create/{parentId}", consumes = {"multipart/form-data"})
    public ApiResponse<ChildResponse> createChildForParent(
            @PathVariable("parentId") Long parentId,
            @RequestParam String fullname,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bod, // Định dạng ngày
            @RequestParam String gender,
            @RequestParam double height,
            @RequestParam double weight,
            @RequestParam RelativeType relationshipType,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) throws IOException {

        ChildCreationRequest request=new ChildCreationRequest(fullname,bod,gender,height,weight,relationshipType);

        ChildResponse child=staffService.createChildForParent(parentId,request,avatar);
//        return ResponseEntity.ok(staffService.createChildForParent(parentId,
//                userJson,avatar));
        return new ApiResponse<>(0, "Child created successfully", child);
    }

    //API Đăng ký  tài khoản staff
    @PreAuthorize("hasAnyRole('ADMIN')")
//    @Operation(summary = "API tạo tài khoản staff(admin)")
//    @PostMapping(value="/createStaff",consumes = {"multipart/form-data"} )
//    public ResponseEntity<ApiResponse<UserResponse>> createStaff(
//            @Schema(description = "{\n" +
//                    "  \"username\": \"tentaikhoan\",\n" +
//                    "  \"fullname\": \"nguyen van a\",\n" +
//                    "  \"password\": \"123456789\",\n" +
//                    "  \"email\": \"dsadsa2@gmail.com\",\n" +
//                    "  \"phone\": \"947325435\",\n" +
//                    "  \"bod\": \"2025-03-08T14:31:04.584Z\",\n" +
//                    "  \"gender\": \"male\"\n" +
//                    "}")
//            @RequestPart("user") String userJson,
//            @RequestPart(value = "avatar", required = false) MultipartFile avatar)
//            throws IOException, JsonProcessingException {
//
//        // Chuyển JSON -> Object
//        UserCreationRequest request = objectMapper.readValue(userJson, UserCreationRequest.class);
//
//        User user = userService.createStaff(request, avatar);
//
//        // Chuyển đổi User -> UserResponse
//        UserResponse userResponse = new UserResponse();
//        userResponse.setId(user.getId());
//        //userResponse.setParentid(user.getParentid());
//        userResponse.setUsername(user.getUsername());
//        userResponse.setEmail(user.getEmail());
//        userResponse.setPhone(user.getPhone());
//        userResponse.setBod(user.getBod());
//        userResponse.setGender(user.getGender());
//        userResponse.setFullname(user.getFullname());
//        userResponse.setAvatarUrl(user.getAvatarUrl()); // Thêm ảnh đại diện nếu có
//
//        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(0, "User created successfully", userResponse);
//        return ResponseEntity.ok(apiResponse);
//    }

    @Operation(summary = "API tạo tài khoản staff(admin)")
    @PostMapping(value = "/createStaff", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<UserResponse>> createStaff(
            // @RequestPart("user") @Valid UserCreationRequest request,
            @RequestParam String username,
            @RequestParam String fullname,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bod, // Định dạng ngày
            @RequestParam String gender,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) throws IOException {

        UserCreationRequest request=new UserCreationRequest(username,
                fullname,
                password,
                email,
                phone,
                bod,
                gender);
        User user = userService.createStaff(request, avatar);

        // Chuyển đổi User -> UserResponse
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setBod(user.getBod());
        userResponse.setGender(user.getGender());
        userResponse.setFullname(user.getFullname());
        userResponse.setAvatarUrl(user.getAvatarUrl()); // Nếu có ảnh

        return ResponseEntity.ok(new ApiResponse<>(0, "User created successfully", userResponse));
    }


        @Operation(summary = "Xóa user",
                description = "CHỉ admin")
        @DeleteMapping("/delete/{id}")
        @PreAuthorize("hasAnyRole('ADMIN','STAFF')") // Chỉ Admin mới có thể xóa user
        public ResponseEntity<String> deleteUser(
                @Parameter(description = "ID of the user to be deleted", required = true)
                @PathVariable Long id) {
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully.");
        }

    @Operation(summary = "Staff tạo tài khoản cho customer",
            description = "Staff tạo tk cho customer . Tk và mk sẽ đc gửi về mail của user ")
    @PreAuthorize("hasAnyRole('STAFF')") // Chỉ staff mới có quyền tạo tài khoản customer
    @PostMapping("/create-customer")
    public ResponseEntity<String> createCustomer(@RequestBody CustomerCreationRequest request) {
        userService.createCustomerByStaff(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Customer account created successfully. Password has been sent to the email.");
    }



    //Active or Unactive staff ? => edit staff ( nhưng chỉ truyền vào 1 param )



}
