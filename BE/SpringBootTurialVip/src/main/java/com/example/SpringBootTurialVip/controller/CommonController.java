package com.example.SpringBootTurialVip.controller;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.request.UserCreationRequest;
import com.example.SpringBootTurialVip.dto.request.VerifyAccountRequest;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.example.SpringBootTurialVip.util.CommonUtil;
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
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/home")
public class CommonController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //APi quên mật khẩu
//    @PostMapping("/forgot-password")
//    public ResponseEntity<?> processForgotPassword(@RequestBody Map<String, String> requestBody,
//                                                   HttpServletRequest request)
//            throws UnsupportedEncodingException, MessagingException {
//
//        String email = requestBody.get("email");
//        if (email == null || email.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Email is required"));
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
    public ResponseEntity<?> processForgotPassword(@RequestBody Map<String, String> requestBody,
                                                   HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {

        String email = requestBody.get("email");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
        }

        User userByEmail = userService.getUserByEmail(email);
        if (ObjectUtils.isEmpty(userByEmail)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid email"));
        }

        String resetToken = UUID.randomUUID().toString();
        userService.updateUserResetToken(email, resetToken);

        String url = CommonUtil.generateUrl(request) + "/reset-password?token=" + resetToken;

        Boolean sendMail = commonUtil.sendMail(url, email);

        if (sendMail) {
            return ResponseEntity.ok(Map.of("message", "Password reset link has been sent to your email"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Something went wrong! Email not sent"));
        }
    }

    //API kiểm tra Reset Password Token
    @GetMapping("/reset-password")
    public ResponseEntity<?> validateResetToken(@RequestParam String token) {

        User userByToken = userService.getUserByToken(token);

        if (userByToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Your link is invalid or expired!"));
        }

        return ResponseEntity.ok(Map.of("message", "Token is valid", "token", token));
    }

    //API  đặt lại password
//    @PostMapping("/reset-password")
//    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> requestBody) {
//
//        String token = requestBody.get("token");
//        String password = requestBody.get("password");
//
//        if (token == null || password == null || password.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("message", "Token and password are required"));
//        }
//
//        User userByToken = userService.getUserByToken(token);
//        if (userByToken == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("message", "Your link is invalid or expired!"));
//        }
//
//        // Cập nhật mật khẩu mới
//        userByToken.setPassword(passwordEncoder.encode(password));
//        userByToken.setResetToken(null); // Xóa token sau khi đặt lại mật khẩu
//        userService.updateUserByResetToken(userByToken);
//
//        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
//    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> requestBody) {

        String token = requestBody.get("token");
        String password = requestBody.get("password");

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

    //API tạo user
    @PostMapping("/createUser")
    ApiResponse<User> createUser(@RequestBody
                                 @Valid  //annotation này dùng đề khai báo cần phải validate object truyền vào phải tuân thủ rule của Object trong server
                                 UserCreationRequest request){
        ApiResponse<User> apiResponse=new ApiResponse<>();

        apiResponse.setResult(userService.createUser(request));

        return apiResponse;
    }

    //Verify account de log in
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
