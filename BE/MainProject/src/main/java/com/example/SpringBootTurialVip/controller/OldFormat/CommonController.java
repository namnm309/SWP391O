package com.example.SpringBootTurialVip.controller.OldFormat;

import com.example.SpringBootTurialVip.dto.request.*;
import com.example.SpringBootTurialVip.dto.response.AuthenticationResponse;
import com.example.SpringBootTurialVip.dto.response.CategoryResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.service.CategoryService;
import com.example.SpringBootTurialVip.service.ProductService;
import com.example.SpringBootTurialVip.service.serviceimpl.AuthenticationServiceImpl;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.example.SpringBootTurialVip.entity.Product;
import com.example.SpringBootTurialVip.util.CommonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
//@RestController
@RequestMapping("/common")
@Tag(name="[Home API]",description = "(Ko cần authen) Các api này sẽ public ở trang chủ ")
public class CommonController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    AuthenticationServiceImpl authenticationServiceImpl;

    //API show ra vaccine khi chưa log in
    @Operation(summary = "API hiển thị danh sách sản phẩm vaccine")
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProduct() {
        List<Product> products = productService.getAllProducts().stream()
                .peek(product -> product.setCategory(product.getCategory())) // Đảm bảo category hiển thị tên
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    //API show ra category khi chưa log in
//    @Operation(summary = "API hiển thị tất cả các danh mục đang hoạt động")
//    @GetMapping("/showActiveCategory")
//    public ResponseEntity<ApiResponse<List<Category>>> showActiveCategory() {
//        List<Category> categories = categoryService.getAllActiveCategory();
//        ApiResponse<List<Category>> response = new ApiResponse<>(1000, "Fetched categories successfully", categories);
//        return ResponseEntity.ok(response);
//    }
    @Operation(summary = "API hiển thị tất cả các danh mục đang hoạt động")
    @GetMapping("/showActiveCategory")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> showActiveCategory() {
        List<CategoryResponse> categories = categoryService.getAllActiveCategory().stream()
                .map(category -> new CategoryResponse(category.getName(), category.getImageName()))
                .collect(Collectors.toList());

        ApiResponse<List<CategoryResponse>> response = new ApiResponse<>(1000, "Fetched categories successfully", categories);
        return ResponseEntity.ok(response);
    }


    //API Đăng nhập ở home
    @Operation(summary = "API login")
    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        var result = authenticationServiceImpl.authencicate(authenticationRequest);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    //API nhập quên mật khẩu
    @PostMapping("/forgot-password")
    public ResponseEntity<?> processForgotPassword(@RequestBody ForgetPasswordRequest request,
                                                   HttpServletRequest httpRequest)
            throws UnsupportedEncodingException, MessagingException {

        //Lấy email cần send code về
        String email = request.getEmail();

        //KO nhập mail
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

        //Check in db có mail này ko
        User userByEmail = userService.getUserByEmail(email);
        if (ObjectUtils.isEmpty(userByEmail)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid email"));
        }

        //Tạo random code để send về mail
        Random random = new Random();
        String code = String.valueOf(random.nextInt(900000) + 100000);


        userByEmail.setResetToken(code);
        userService.updateUserByResetToken(userByEmail);

        Boolean sendMail = commonUtil.sendMail(code, email);

        if (sendMail) {
            return ResponseEntity.ok(Map.of("message", "Password reset link has been sent to your email"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Something went wrong! Email not sent"));
        }
    }


    //API kiểm tra Reset Password Token dành cho Front End
    @Operation(summary = "API này kiểm tra xem link gửi về mail và thông báo cho FE ",
    description = "Có thể ko xài cái này tại !")
    @GetMapping("/reset-password")
    public ResponseEntity<?> validateResetToken(@RequestParam String token) {

        User userByToken = userService.getUserByToken(token);

        if (userByToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Your link is invalid or expired!"));
        }

        return ResponseEntity.ok(Map.of("message", "Token is valid", "token", token));
    }

    //API Nhập lại password dành cho customer
    @Operation(summary = "API nhập lại mật khẩu dựa trên token gửi qua email")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {

        String token = request.getToken();
        String password = request.getPassword();

        if (token == null || password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Token and password are required"));
        }

        User userByToken = userService.getUserByToken(token);
        if (userByToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Your link is invalid or expired!"));
        }

        // Cập nhật mật khẩu mới
        userByToken.setPassword(passwordEncoder.encode(password));
        userByToken.setResetToken(null); // Xóa token sau khi đặt lại mật khẩu
        userService.updateUserByResetToken(userByToken);

        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }



    //API Đăng ký  tài khoản ở home
//    @Operation(summary = "API tạo tài khoản")
//    @PostMapping("/createUser")
//    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody @Valid UserCreationRequest request) {
//        User user = userService.createUser(request);
//
//        // Chuyển đổi User -> UserResponse
//        UserResponse userResponse = new UserResponse();
//        userResponse.setId(user.getId());
//        userResponse.setParentid(user.getParentid());
//        userResponse.setUsername(user.getUsername());
//        userResponse.setEmail(user.getEmail());
//        userResponse.setPhone(user.getPhone());
//        userResponse.setBod(user.getBod());
//        userResponse.setGender(user.getGender());
//        userResponse.setFullname(user.getFullname());
//
//        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(0, "User created successfully", userResponse);
//        return ResponseEntity.ok(apiResponse);
//    }


    //API resend mã code xác thực qua email
    @Operation(summary = "API nhận lại mã xác thực để verify account đăng ký")
    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestBody ResendVerificationRequest request) {
        try {
            String email = request.getEmail(); // Lấy email từ DTO
            userService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //API Verify account để log in
    @Operation(summary = "API xác thực tài khoản sau bước đăng ký ",
            description = "API đăng ký nằm ở Common Controller")
    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody
                                        @Valid
                                        VerifyAccountRequest verifyAccountRequest) {
        try {
            userService.verifyUser(verifyAccountRequest);
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }





}
