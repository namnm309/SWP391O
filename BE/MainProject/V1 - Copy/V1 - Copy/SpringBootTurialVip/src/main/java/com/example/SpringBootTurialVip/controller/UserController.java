package com.example.SpringBootTurialVip.controller;

import com.example.SpringBootTurialVip.dto.request.*;
import com.example.SpringBootTurialVip.dto.response.AuthenticationResponse;
import com.example.SpringBootTurialVip.dto.response.ChildCreationResponse;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")//do user dùng chung nhiều khai bóa ở đây ở dưới sẽ ko cần
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

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

    //Resend ma verifinecode to email
//    @PostMapping("/resend")
//    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
//        try {
//            userService .resendVerificationCode(email);
//            return ResponseEntity.ok("Verification code sent");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email"); // Lấy email từ JSON
            userService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //API lấy danh sách user
    @GetMapping
    List<User> getUsers(){
        //Để get thông tin hiện tại đang đc authenticated , chứa thông tin user đang log in hiện tại
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        //In ra thông tin trong console
        log.info("Username: {}",authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority ->
                log.info(grantedAuthority.getAuthority()));

        return userService.getUsers();
    }

    //API lấy thông tin 1 user
//        @GetMapping("/{userId}")//Nhận 1 param id để tìm thông tin user đó
//        User getUser(@PathVariable("userId") String userId){
//            return userService.getUserById(userId);
//        }

    @GetMapping("/{userId}")//Nhận 1 param id để tìm thông tin user đó
    UserResponse getUser(@PathVariable("userId") String userId){
        return userService.getUserById(userId);
    }

//    @GetMapping("/username/{username}")
//    Optional<User> getUserName(@PathVariable("username") String username){
//        return userService.getUserName(username);
//    }

    //API update thông tin user
//        @PutMapping("/{userId}")//update dựa trên ID
//        User updateUser(@PathVariable String userId ,@RequestBody UserUpdateRequest updateRequest){//Tạo 1 object request mới
//            return userService.updateUser(userId,updateRequest);
//        }
    @PutMapping("/{userId}")//update dựa trên ID
    UserResponse updateUser(@PathVariable String userId ,@RequestBody UserUpdateRequest updateRequest){//Tạo 1 object request mới
        return userService.updateUser(userId,updateRequest);
    }

    //API xóa thông tin user
    @DeleteMapping("/delete/{userId}")
    String deleteUser(@PathVariable("userId") String userID){
        userService.deleteUser(userID);
        return "Đã delete user và danh sách sau khi delete là : ";
    }

    //Thông tin cá nhân
    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    //Tạo hồ sơ trẻ em
    @PostMapping("/child/create")
    ApiResponse<User> createChild(@RequestBody
                                  @Valid
                                  ChildCreationRequest childCreationRequest){

        ApiResponse<User> apiResponse=new ApiResponse<>();

        apiResponse.setResult(userService.createChild(childCreationRequest));

        return apiResponse;
    }

    @GetMapping("/my-children")
    public ResponseEntity<List<ChildResponse>> getMyChildren() {
        return ResponseEntity.ok(userService.getChildInfo());
    }

    @PutMapping("/update-my-children")
    public ResponseEntity<List<ChildResponse>> updateMyChildren(@RequestBody ChildCreationRequest request) {
        return ResponseEntity.ok(userService.updateChildrenByParent(request));
    }

}
