package com.example.SpringBootTurialVip.controller;

import com.example.SpringBootTurialVip.dto.request.*;
import com.example.SpringBootTurialVip.dto.response.AuthenticationResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.service.CategoryService;
import com.example.SpringBootTurialVip.service.ProductService;
import com.example.SpringBootTurialVip.service.serviceimpl.AuthenticationServiceImpl;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.example.SpringBootTurialVip.shopentity.Category;
import com.example.SpringBootTurialVip.shopentity.Product;
import com.example.SpringBootTurialVip.util.CommonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
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
        return ResponseEntity.ok(productService.getAllProducts());
    }

    //API show ra category khi chưa log in
//    @Operation(summary = "API hiển thị danh mục vaccine",
//    description = "Ví dụ : Vaccine lẻ , Vaccine gói . 3 danh mục hoặc nhiều hơn có thể tạo ")
//    @GetMapping("/showCategory")
//    public ResponseEntity<ApiResponse<List<Category>>> loadAddProduct() {
//        List<Category> categories = categoryService.getAllCategory();
//        ApiResponse<List<Category>> response = new ApiResponse<>(1000, "Fetched categories successfully", categories);
//        return ResponseEntity.ok(response);
//    }
    @Operation(summary = "API hiển thị tất cả các danh mục đang hoạt động")
    @GetMapping("/showActiveCategory")
    public ResponseEntity<ApiResponse<List<Category>>> showActiveCategory() {
        List<Category> categories = categoryService.getAllActiveCategory();
        ApiResponse<List<Category>> response = new ApiResponse<>(1000, "Fetched categories successfully", categories);
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
//    @Operation(summary = "API quên mật khẩu",
//    description = "Yêu cầu cung cấp email để hệ thống check và gửi link về mail," +
//            "Sẽ gửi : http://localhost:8080/reset-password?token=cafc80d7-efd8-440b-b39f-8c4ac5d0c4f1 về mail"+
//    "có gì mấy anh tạo giúp em cái page để show link này ra với")
//    @PostMapping("/forgot-password")
//    public ResponseEntity<?> processForgotPassword(@RequestBody Map<String, String> requestBody,
//                                                   HttpServletRequest request)
//            throws UnsupportedEncodingException, MessagingException {
//
//        String email = requestBody.get("email");
//
//        if (email == null || email.isEmpty()) {
//            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
//        }
//
//        User userByEmail = userService.getUserByEmail(email);
//        if (ObjectUtils.isEmpty(userByEmail)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid email"));
//        }
//
//        String resetToken = UUID.randomUUID().toString();
//        userService.updateUserResetToken(email, resetToken);
//
//        String url = CommonUtil.generateUrl(request) + "/reset-password?token=" + resetToken;
//
//        Boolean sendMail = commonUtil.sendMail(url, email);
//
//        if (sendMail) {
//            return ResponseEntity.ok(Map.of("message", "Password reset link has been sent to your email"));
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("message", "Something went wrong! Email not sent"));
//        }
//    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> processForgotPassword(@RequestBody ForgetPasswordRequest request,
                                                   HttpServletRequest httpRequest)
            throws UnsupportedEncodingException, MessagingException {

        String email = request.getEmail();

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

        User userByEmail = userService.getUserByEmail(email);
        if (ObjectUtils.isEmpty(userByEmail)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid email"));
        }

        String resetToken = UUID.randomUUID().toString();
        userService.updateUserResetToken(email, resetToken);

        String url = CommonUtil.generateUrl(httpRequest) + "/reset-password?token=" + resetToken;
        Boolean sendMail = commonUtil.sendMail(url, email);

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
    @Operation(summary = "API tạo tài khoản")
    @PostMapping("/createUser")
    ApiResponse<User> createUser(@RequestBody
                                 @Valid  //annotation này dùng đề khai báo cần phải validate object truyền vào phải tuân thủ rule của Object trong server
                                 UserCreationRequest request){
        ApiResponse<User> apiResponse=new ApiResponse<>();

        apiResponse.setResult(userService.createUser(request));

        return apiResponse;
    }

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
