package com.example.SpringBootTurialVip.controller;

import com.example.SpringBootTurialVip.dto.request.*;
import com.example.SpringBootTurialVip.dto.response.ChildResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.service.CartService;
import com.example.SpringBootTurialVip.service.OrderService;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.example.SpringBootTurialVip.shopentity.Cart;
import com.example.SpringBootTurialVip.shopentity.OrderRequest;
import com.example.SpringBootTurialVip.util.CommonUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/users")//do user dùng chung nhiều khai bóa ở đây ở dưới sẽ ko cần
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

//    //API tạo user
//    @PostMapping("/createUser")
//    ApiResponse<User> createUser(@RequestBody
//                    @Valid  //annotation này dùng đề khai báo cần phải validate object truyền vào phải tuân thủ rule của Object trong server
//                    UserCreationRequest request){
//        ApiResponse<User> apiResponse=new ApiResponse<>();
//
//        apiResponse.setResult(userService.createUser(request));
//
//        return apiResponse;
//    }
//
//    //Verify account de log in
//    @PostMapping("/verify")
//    public ResponseEntity<?> verifyUser(@RequestBody
//                                            @Valid
//                                            VerifyAccountRequest verifyAccountRequest) {
//        try {
//            userService.verifyUser(verifyAccountRequest);
//            return ResponseEntity.ok("Account verified successfully");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

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
    UserResponse getUser(@PathVariable("userId") Long userId){
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
    @PutMapping("/{userId}")//Update dựa trên ID
    UserResponse updateUser(@PathVariable Long userId ,@RequestBody UserUpdateRequest updateRequest){//Tạo 1 object request mới
        return userService.updateUser(userId,updateRequest);
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

    //API : Cart
    @PostMapping("/addCart")
    public ResponseEntity<ApiResponse<Cart>> addToCart(@RequestParam Long pid, @RequestParam Long uid) {
        Cart savedCart = cartService.saveCart(pid, uid);

        if (ObjectUtils.isEmpty(savedCart)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(1004, "Product add to cart failed", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(1000, "Product added to cart", savedCart));
    }

    //API order vaccine
    private UserResponse getLoggedInUserDetails() {

        UserResponse user = userService.getMyInfo();
        return user;
    }

    //API xem cart khách hàng đã thêm vài  dựa theo token truy ra thông tin cá nhân
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOrderPage() {
        UserResponse user = getLoggedInUserDetails();
        List<Cart> carts = cartService.getCartsByUser(user.getId());

        // Chuẩn bị dữ liệu phản hồi
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("carts", carts);

        if (!carts.isEmpty()) {
            Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            Double totalOrderPrice = orderPrice + 250 + 100;
            responseData.put("orderPrice", orderPrice);
            responseData.put("totalOrderPrice", totalOrderPrice);
        }

        return ResponseEntity.ok(new ApiResponse<>(1000, "Order details fetched successfully", responseData));
    }


    //API tạo order dựa trên token truy ra thông tin cá nhân để lưu vào
    @PostMapping("/saveOrder")
    public ResponseEntity<ApiResponse<String>> saveOrder(@RequestParam Long cartId, @RequestBody OrderRequest orderRequest) {
        try {
            orderService.saveOrder(cartId, orderRequest);
            return ResponseEntity.ok(new ApiResponse<>(1000, "Order saved successfully", null));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(1004, "Error: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(1004, "Unexpected error: " + e.getMessage(), null));
        }
    }










}
