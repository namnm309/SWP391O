package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.*;
import com.example.SpringBootTurialVip.dto.response.AuthenticationResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.dto.response.VerifyTokenResponse;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.exception.AppException;
import com.example.SpringBootTurialVip.exception.ErrorCode;
import com.example.SpringBootTurialVip.service.serviceimpl.AuthenticationServiceImpl;
import com.example.SpringBootTurialVip.service.serviceimpl.FileStorageService;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.example.SpringBootTurialVip.util.CommonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Random;

//Class này verify user cung cấp password đã mã hóa có đúng ko
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor//Autowired các bean
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)//ko khai báo thì các field sẽ đc override thành private và final
@Tag(name="[Authentication API]",description = "(Ko cần authen) Các api về xác thực và đăng nhập ")
//@PreAuthorize("hasAnyRole('CUSTOMER','STAFF', 'ADMIN')")
public class AuthenticationController {
    //Tạo 2 dto
    //dto AuthenticationRequest để user cung cấp username và passworrd để log in
    // dto AuthenticationResponse để xem user có cung cấp đúng tk mk ko

    //Tạo AuthenticationService
    @Autowired
    AuthenticationServiceImpl authenticationServiceImpl;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CommonUtil commonUtil;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FileStorageService fileStorageService;

    //API Đăng ký  tài khoản ở home
    @Operation(summary = "API tạo tài khoản ")
    @PostMapping("/createUser")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody
                                                                    @Valid
                                                                    UserCreationRequest request) {
        MultipartFile image = null;
        User user = userService.createUser(request,image);

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

//    @Operation(summary = "API tạo tài khoản tét ")
//    @PostMapping("/createUser")
//    public ResponseEntity<ApiResponse<UserResponse>> createTest(@RequestBody
//                                                                @Valid
//                                                                UserCreationRequest request) {
//        MultipartFile image = null;
//        User user = userService.createUser(request,image);
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

//    @Operation(summary = "API tạo tài khoản", description = "Tạo tài khoản mới với thông tin người dùng và ảnh đại diện")
//    @PostMapping(value = "/createUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ApiResponse<UserResponse>> createUser(
//            @RequestParam String username,
//            @RequestParam String fullname,
//            @RequestParam String password,
//            @RequestParam String email,
//            @RequestParam String phone,
//            @RequestParam Date bod,
//            @RequestParam String gender,
//            @RequestParam(required = false) MultipartFile image)
//            throws IOException {
//
//        UserCreationRequest user = new UserCreationRequest();
//        user.setUsername(username);
//        user.setFullname(fullname);
//        user.setPassword(password);
//        user.setEmail(email);
//        user.setPhone(phone);
//        user.setBod(bod);
//        user.setGender(gender);
//
//        // Nếu có file ảnh, upload lên Cloudinary
//        if (image != null && !image.isEmpty()) {
//            try {
//                String imageUrl = fileStorageService.uploadFile(image);
//                user.setImage(imageUrl); // Lưu URL ảnh vào User
//            } catch (IOException e) {
//                user.setImage("null");
//            }
//        }
//
//         User users = userService.createUser(user,image);
//
//        // Chuyển đổi User -> UserResponse
//        UserResponse userResponse = new UserResponse();
//        userResponse.setId(users.getId());
//        userResponse.setParentid(users.getParentid());
//        userResponse.setUsername(users.getUsername());
//        userResponse.setEmail(users.getEmail());
//        userResponse.setPhone(users.getPhone());
//        userResponse.setBod(users.getBod());
//        userResponse.setGender(users.getGender());
//        userResponse.setFullname(users.getFullname());
//        userResponse.setAvatarUrl(users.getAvatarUrl());
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

    //API login
    @Operation(summary = "API đăng nhập")
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

    //API xác thực token ( chỉ test )
    @Operation(summary = "[Không Xài]API phục vụ mục đích test token")
    @PostMapping("/verifyToken")
    ApiResponse<VerifyTokenResponse> authenticate(@RequestBody VerifyTokenRequest request) throws ParseException, JOSEException {
        var result = authenticationServiceImpl.verifyTokenResponse(request);

        return ApiResponse.<VerifyTokenResponse>builder()
                .result(result)
                .build();

    }











}
