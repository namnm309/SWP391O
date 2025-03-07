package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.*;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.service.AuthenticationService;
import com.example.SpringBootTurialVip.service.StaffService;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.example.SpringBootTurialVip.util.CommonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name="[User]",description = "")
@PreAuthorize("hasAnyRole('CUSTOMER','STAFF', 'ADMIN')")
@Slf4j
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StaffService staffService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private AuthenticationService authenticationService;



    //API hiển thị thông tin cá nhân để truy xuất giỏ hàng và ... - OK
    @Operation(summary = "API hiển thị profile")
    private UserResponse getLoggedInUserDetails() {
        UserResponse user = userService.getMyInfo();
        return user;
    }

    //API tạo hồ sơ trẻ em - OK
    @Operation(summary = "API tạo hồ sơ trẻ em, dựa theo token để xác định parent")
    @PostMapping("/child/create")
    public ApiResponse<ChildResponse> createChild(@RequestBody @Valid ChildCreationRequest childCreationRequest) {

        // Lấy thông tin user đang đăng nhập
        UserResponse loggedInUser = getLoggedInUserDetails();
        Long parentId = loggedInUser.getId();

        // Gán parentId vào request
        childCreationRequest.setParentid(parentId);

        // Gọi service để tạo child
        ChildResponse childResponse = userService.addChild(childCreationRequest);

        // Trả về response
        return new ApiResponse<>(0, "Child created successfully", childResponse);
    }

    //API xem hồ sơ trẻ em ( dựa theo token ) - OK
    @Operation(summary = "Xem thông tin trẻ của mình ")
    @GetMapping("/my-children")
    public ResponseEntity<List<ChildResponse>> getMyChildren() {
        return ResponseEntity.ok(userService.getChildInfo());
    }

//    @Operation(summary = "Cập nhật thông tin cho trẻ (customer) ",description = "Truyền vào userid là id của trẻ , parentid se")
//    @PutMapping("/update-my-children")
//    public ResponseEntity<ChildResponse> updateMyChildren(@RequestBody @Valid ChildUpdateRequest request) {
//        ChildResponse updatedChild = userService.updateChildrenByParent(request);
//        return ResponseEntity.ok(updatedChild);
//    }



    //API Xem thông tin cá nhân - OK
    @Operation(summary = "APi xem profile")
    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    //API update thông tin user
    @Operation(summary = "API cập nhật thông tin cá nhân")
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody
                                               UpdateProfileRequest userDetails

    ) {
        // Lấy thông tin từ SecurityContextHolder
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = jwt.getClaim("email"); // Lấy email từ token

        System.out.println("DEBUG: Extracted email from token = " + email);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User email not found in token");
        }

        // Lấy thông tin người dùng từ database
        User existingUser = userService.getUserByEmail(email);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with email: " + email);
        }

        // Cập nhật thông tin mới (chỉ cho phép cập nhật một số trường)
        existingUser.setPhone(userDetails.getPhone());
        existingUser.setFullname(userDetails.getFullname());
        existingUser.setBod(userDetails.getBod());
        existingUser.setGender(userDetails.getGender());

        // Nếu user muốn đổi password, phải mã hóa
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        // Lưu lại thông tin đã cập nhật
        User updatedUser = userService.updateUser(existingUser);
// Chuyển đổi User -> UserResponse
        UserResponse userResponse = new UserResponse();

        userResponse.setUsername(updatedUser.getUsername());
        userResponse.setEmail(updatedUser.getEmail());
        userResponse.setPhone(updatedUser.getPhone());
        userResponse.setBod(updatedUser.getBod());
        userResponse.setGender(updatedUser.getGender());
        userResponse.setFullname(updatedUser.getFullname());

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(0, "User created successfully", userResponse);
        return ResponseEntity.ok(apiResponse);
    }



    //API: Update(Edit) thông tin `Child`
    @Operation(summary = "Cập nhật thông tin trẻ dựa theo ID", description = "API này cho phép cập nhật thông tin của một đứa trẻ dựa vào ID.")
    @PutMapping("/children/{childId}/update")
    public ResponseEntity<ApiResponse<ChildResponse>> updateChildInfo(
            @PathVariable Long childId,
            @RequestBody @Valid ChildCreationRequest request) {

        try {
            ChildResponse updatedChild = staffService.updateChildInfo(childId, request);
            return ResponseEntity.ok(new ApiResponse<>(0, "Cập nhật thành công", updatedChild));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(e.getErrorCode().getCode(), e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(500, "Lỗi hệ thống", null));
        }
    }


    //API : tìm user = id
    //API tìm kiếm user
    @Operation(summary = "APi tìm kiếm 1 user = user id ")
    @GetMapping("/{userId}")
    //Nhận 1 param id để tìm thông tin user đó
    UserResponse getUser(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    //API tìm kiếm trẻ = userid ,
    @Operation(summary = "Lấy thông tin một trẻ theo userId", description = "API này chỉ trả về thông tin của trẻ nếu người dùng có quan hệ với trẻ đó.")
    @GetMapping("/child/{id}")
    public ResponseEntity<ApiResponse<ChildResponse>> getChildByUserId(@PathVariable Long id) {
        try {
            ChildResponse childResponse = userService.getChildByUserId(id);
            return ResponseEntity.ok(new ApiResponse<>(0, "Lấy thông tin trẻ thành công", childResponse));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(e.getErrorCode().getCode(), e.getMessage(), null));
        }
    }











}
