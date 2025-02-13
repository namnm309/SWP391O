package com.example.SpringBootTurialVip.controller;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.request.UserCreationRequest;
import com.example.SpringBootTurialVip.dto.request.UserUpdateRequest;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


}
