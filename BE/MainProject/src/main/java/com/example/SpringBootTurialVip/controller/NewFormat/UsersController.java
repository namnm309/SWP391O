package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.*;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.service.AuthenticationService;
import com.example.SpringBootTurialVip.service.StaffService;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.example.SpringBootTurialVip.util.CommonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @Operation(summary = "API tạo hồ sơ trẻ em , dựa theo token để xđ parent")
    @PostMapping("/child/create")
    public ApiResponse<UserResponse> createChild(@RequestBody
                                         @Valid
                                         ChildCreationRequest childCreationRequest) {
        ApiResponse<User> apiResponse = new ApiResponse<>();

        // Lấy thông tin user đang đăng nhập
        UserResponse loggedInUser = getLoggedInUserDetails();
        Long parentId = loggedInUser.getId();

        // Tạo object mới với parentId
        ChildCreationRequest updatedRequest = ChildCreationRequest.builder()
                .fullname(childCreationRequest.getFullname())
                .bod(childCreationRequest.getBod())
                .gender(childCreationRequest.getGender())
                .height(childCreationRequest.getHeight())
                .weight(childCreationRequest.getWeight())
                .parentid(parentId) // Gán parentId từ user đăng nhập
                .build();

        // Gọi service để tạo child
        User user=userService.createChild(updatedRequest);

        UserResponse userResponse = new UserResponse();

        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setBod(user.getBod());
        userResponse.setGender(user.getGender());
        userResponse.setFullname(user.getFullname());

        ApiResponse<UserResponse> apiiResponse = new ApiResponse<>(0, "User created successfully", userResponse);
        return apiiResponse;


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
    @Operation(
            summary = "Update thông tin trẻ dựa theo ID",
            description = "API này cho phép cập nhật thông tin của một đứa trẻ dựa vào ID."
    )
    @PutMapping("/children/{childId}/update")
    public ResponseEntity<ChildResponse> updateChildInfo(
            @PathVariable Long childId,
            @RequestBody ChildCreationRequest request) {

        ChildResponse updatedChild = staffService.updateChildInfo(childId, request);

        if (updatedChild == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedChild);
    }







//    //API Đăng nhập ở home
//    @Operation(summary = "API login")
//    @PostMapping("/login")
//    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
//        var result = authenticationService.authencicate(authenticationRequest);
//
//        return ApiResponse.<AuthenticationResponse>builder()
//                .result(result)
//                .build();
//    }

//    //API nhập quên mật khẩu
//    @PostMapping("/forgot-password")
//    public ResponseEntity<?> processForgotPassword(@RequestBody ForgetPasswordRequest request,
//                                                   HttpServletRequest httpRequest)
//            throws UnsupportedEncodingException, MessagingException {
//
//        //Lấy email cần send code về
//        String email = request.getEmail();
//
//        //KO nhập mail
//        if (email == null || email.isEmpty()) {
//            return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
//        }
//
//        //Check in db có mail này ko
//        User userByEmail = userService.getUserByEmail(email);
//        if (ObjectUtils.isEmpty(userByEmail)) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid email"));
//        }
//
//        //Tạo random code để send về mail
//        Random random = new Random();
//        String code = String.valueOf(random.nextInt(900000) + 100000);
//
//
//        userByEmail.setResetToken(code);
//        userService.updateUserByResetToken(userByEmail);
//
//        Boolean sendMail = commonUtil.sendMail(code, email);
//
//        if (sendMail) {
//            return ResponseEntity.ok(Map.of("message", "Password reset link has been sent to your email"));
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("message", "Something went wrong! Email not sent"));
//        }
//    }
//
//
//    //API kiểm tra Reset Password Token dành cho Front End
//    @Operation(summary = "API này kiểm tra xem link gửi về mail và thông báo cho FE ",
//            description = "Có thể ko xài cái này tại !")
//    @GetMapping("/reset-password")
//    public ResponseEntity<?> validateResetToken(@RequestParam String token) {
//
//        User userByToken = userService.getUserByToken(token);
//
//        if (userByToken == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("message", "Your link is invalid or expired!"));
//        }
//
//        return ResponseEntity.ok(Map.of("message", "Token is valid", "token", token));
//    }
//
//    //API Nhập lại password dành cho customer
//    @Operation(summary = "API nhập lại mật khẩu dựa trên token gửi qua email")
//    @PostMapping("/reset-password")
//    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
//
//        String token = request.getToken();
//        String password = request.getPassword();
//
//        if (token == null || password == null || password.isEmpty()) {
//            return ResponseEntity.badRequest().body(Map.of("message", "Token and password are required"));
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



//    //API Đăng ký  tài khoản ở home
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
//
//
//
//    //API resend mã code xác thực qua email
//    @Operation(summary = "API nhận lại mã xác thực để verify account đăng ký")
//    @PostMapping("/resend")
//    public ResponseEntity<?> resendVerificationCode(@RequestBody ResendVerificationRequest request) {
//        try {
//            String email = request.getEmail(); // Lấy email từ DTO
//            userService.resendVerificationCode(email);
//            return ResponseEntity.ok("Verification code sent");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    //API Verify account để log in
//    @Operation(summary = "API xác thực tài khoản sau bước đăng ký ",
//            description = "API đăng ký nằm ở Common Controller")
//    @PostMapping("/verify")
//    public ResponseEntity<?> verifyUser(@RequestBody
//                                        @Valid
//                                        VerifyAccountRequest verifyAccountRequest) {
//        try {
//            userService.verifyUser(verifyAccountRequest);
//            return ResponseEntity.ok("Account verified successfully");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
}
