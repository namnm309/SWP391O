package com.example.SpringBootTurialVip.controller.Format;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.response.OrderResponse;
import com.example.SpringBootTurialVip.dto.response.ProductOrderResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.Cart;
import com.example.SpringBootTurialVip.entity.OrderRequest;
import com.example.SpringBootTurialVip.entity.ProductOrder;
import com.example.SpringBootTurialVip.enums.OrderStatus;
import com.example.SpringBootTurialVip.service.CartService;
import com.example.SpringBootTurialVip.service.OrderService;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.example.SpringBootTurialVip.util.CommonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
@Tag(name="[Order]",description = "")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    //APi sp cho xem giỏ hàng
    //API hiển thị thông tin cá nhân để truy xuất giỏ hàng và ... - OK
    //@Operation(summary = "API hiển thị profile")
    private UserResponse getLoggedInUserDetails() {
        UserResponse user = userService.getMyInfo();
        return user;
    }

    //API cập nhật  tình trạng đơn hàng
    @Operation(summary = "API cập nhật trạng thái đơn hàng = id đơn hàng",description =
            "StatusID list : (1,In Progress) \n"+
                    "(2,Order Received) \n" +
                    "(3, Out for Stock) \n" +
                    "(4,Cancelled) \n" +
                    "(5,Success) \n")
    @PutMapping("/update-status")
    public ResponseEntity<ApiResponse<ProductOrder>> updateOrderStatus(
            @RequestParam Long id,//ID đơn hàng
            @RequestParam Integer statusId) {

        //Tìm `OrderStatus` nhanh hơn bằng Stream API
        Optional<OrderStatus> matchedStatus = Arrays.stream(OrderStatus.values())
                .filter(orderStatus -> orderStatus.getId().equals(statusId))
                .findFirst();

        if (matchedStatus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(1001, "Invalid status ID", null));
        }

        String status = matchedStatus.get().getName();

        // Cập nhật trạng thái đơn hàng
        ProductOrder updatedOrder = orderService.updateOrderStatus(id, status);

        if (updatedOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(1002, "Order not found or status not updated", null));
        }

        // Gửi email thông báo nếu cần
        try {
            commonUtil.sendMailForProductOrder(updatedOrder, status);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(1003, "Order updated but email failed", updatedOrder));
        }

        return ResponseEntity.ok(new ApiResponse<>(1000, "Order status updated successfully", updatedOrder));
    }

    //API xem cart khách hàng đã thêm vài  dựa theo token truy ra thông tin cá nhân
    @Operation(summary = "API trả về danh sách sản phẩm trong bước thanh toán , chỉ khác /cart cách format , xài nào cũng dc9 ")
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrderPage() {
        UserResponse user = getLoggedInUserDetails();
        List<Cart> carts = cartService.getCartsByUser(user.getId());

        // Chuyển danh sách Cart sang DTO OrderResponse
        List<OrderResponse> cartResponses = carts.stream()
                .map(cart -> new OrderResponse(
                        cart.getId(),
                        cart.getProduct(),
                        cart.getQuantity(),
                        cart.getTotalPrice(),
                        cart.getTotalOrderPrice()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(1000, "Order details fetched successfully", cartResponses));
    }


    //API lưu đơn hàng , đặt hàng từ cart id
    @Operation(summary = "API này sẽ nhận cartId và tiến hành đặt hàng lưu vào db")
    @PostMapping("/saveOrder")
    public ResponseEntity<ApiResponse<String>> saveOrder(@RequestParam Long cid, @RequestBody OrderRequest orderRequest) {
        try {
            // Lấy thông tin user đang đăng nhập
            UserResponse loginUser = getLoggedInUserDetails();
            Long loggedInUserId = loginUser.getId();
            log.info(String.valueOf("id của user đang log là : "+loggedInUserId));

            // Tìm giỏ hàng (Cart) theo cartId
            Cart cart = cartService.getCartById(cid);

            // Kiểm tra xem cart có tồn tại không
            if (cart == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(1004, "Error: Cart ID not found", null));
            }

            // Kiểm tra xem giỏ hàng có thuộc về user đang đăng nhập không
            if (!cart.getUser().getId().equals(loggedInUserId)) {
                log.info("Kết quả so sánh là : "+Boolean.toString(!cart.getUser().getId().equals(loggedInUserId)));
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(1003, "Error: You do not have permission to access this cart", null));
            }

            // Nếu đúng user, tiếp tục lưu đơn hàng
            orderService.saveOrder(cid, orderRequest);

            return ResponseEntity.ok(new ApiResponse<>(1000, "Order saved successfully", null));

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(1004, "Error: Cart ID not found - " + e.getMessage(), null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(1004, "Error: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(1004, "Unexpected error: " + e.getMessage(), null));
        }
    }


    //API xem đơn hàng đã đặt
    @Operation(summary = "APi xem đơn hàng đã đặt ")
    @GetMapping("/user-orders")
    public ResponseEntity<ApiResponse<List<ProductOrderResponse>>> myOrder() {
        UserResponse loginUser = getLoggedInUserDetails();
        List<ProductOrder> orders = orderService.getOrdersByUser(loginUser.getId());

        if (loginUser == null || loginUser.getId() == null) {
            log.error("Failed to retrieve logged-in user or user ID is null.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(1001, "Unauthorized: Cannot retrieve user", null));
        }


        // Chuyển đổi danh sách `ProductOrder` sang `ProductOrderResponse` để ẩn thông tin nhạy cảm
        List<ProductOrderResponse> orderResponses = orders.stream()
                .map(order -> new ProductOrderResponse(
                        order.getOrderId(),
                        order.getOrderDate(),
                        order.getProduct(),
                        order.getPrice(),
                        order.getQuantity(),
                        order.getStatus(),
                        order.getPaymentType(),
                        order.getOrderDetail() // Gửi thông tin OrderDetail nếu cần
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(1000, "User orders retrieved successfully", orderResponses));
    }
}
