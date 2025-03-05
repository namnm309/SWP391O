package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.UpdateCartQuantityRequest;
import com.example.SpringBootTurialVip.dto.response.CartResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.Cart;
import com.example.SpringBootTurialVip.repository.CartRepository;
import com.example.SpringBootTurialVip.service.CartService;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name="[Cart]",description = "")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartRepository cartRepository;

    //API thêm vào giỏ hàng
    @PreAuthorize("hasAnyRole('CUSTOMER','TEST')")
    @Operation(summary = "API thêm sản phẩm vào giỏ hàng và lưu vào db (customer)")
    @PostMapping("/addCart")
    public ResponseEntity<String> addToCart(@RequestParam("pid") Long productId) {
        try {
            // Lấy Authentication từ SecurityContext
            Long userid=userService.getMyInfo().getId();

            if (userid == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            cartService.saveCart(productId,userid);

            return ResponseEntity.ok("Product added to cart successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    //APi sp cho xem giỏ hàng
    //API hiển thị thông tin cá nhân để truy xuất giỏ hàng và ... - OK
    //@Operation(summary = "API hiển thị profile")
    private UserResponse getLoggedInUserDetails() {
        UserResponse user = userService.getMyInfo();
        return user;
    }

    //API xem giỏ hàng - OK
    @PreAuthorize("hasAnyRole('CUSTOMER','TEST')")
    @Operation(summary = "API xem giỏ hàng trước khi thanh toán (customer) ")
    @GetMapping("/cart")
    public ResponseEntity<?> loadCartPage() {

        UserResponse user = getLoggedInUserDetails();
        List<Cart> carts = cartService.getCartsByUser(user.getId());

        // Chuyển danh sách Cart sang DTO để ẩn thông tin User
        List<CartResponse> cartResponses = carts.stream()
                .map(cart -> new CartResponse(
                        cart.getId(),
                        cart.getProduct(),
                        cart.getQuantity(),
                        cart.getTotalPrice(),
                        cart.getTotalOrderPrice()
                ))
                .collect(Collectors.toList());

        if (carts.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "message", "Your cart is empty",
                    "carts", Collections.emptyList(),
                    "totalOrderPrice", 0
            ));
        }

        Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();

        return ResponseEntity.ok(Map.of(
                "message", "Cart retrieved successfully",
                "carts", cartResponses,
                "totalOrderPrice", totalOrderPrice
        ));
    }

    //API cập nhật giỏ hàng ( khách hàng thêm hoặc bớt sản phẩm ) , nếu giảm = 0 sẽ xóa cart - OK
    @PreAuthorize("hasAnyRole('CUSTOMER','TEST')")
    @Operation(summary = "APi updatte giỏ hàng (cho customer)",
            description = "Turial FE:" +
                    "APi nhận 2 param là : sy(action thêm hoặc bớt 1 sp, nếu = 0 sẽ xóa khỏi cart" +
                    "cid là cart id " +
                    "Anh giúp em xử lý hành động sy như thế này với ạ : thêm là String:'increase' và giảm là 'decrease' nếu giảm còn api sẽ thông báo và xóa item đó ra khỏi cart ")

    @PutMapping("/cart/update-quantity")
    public ResponseEntity<?> updateCartQuantity(@Valid @RequestBody UpdateCartQuantityRequest request) {

        String sy = request.getSy();
        Long cid = request.getCid();

        Cart cart = cartRepository.findById(cid).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Cart item not found"));
        }

        int currentQuantity = cart.getQuantity();

        // Xử lý tăng/giảm số lượng
        if ("increase".equalsIgnoreCase(sy)) {
            cart.setQuantity(currentQuantity + 1);
        } else if ("decrease".equalsIgnoreCase(sy)) {
            if (currentQuantity > 1) {
                cart.setQuantity(currentQuantity - 1);
            } else {
                cartRepository.delete(cart);
                return ResponseEntity.ok(Map.of("message", "Product removed from cart"));
            }
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid operation type"));
        }

        cartRepository.save(cart);
        return ResponseEntity.ok(Map.of("message", "Cart quantity updated successfully", "quantity", cart.getQuantity()));
    }



}
