package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.response.OrderResponse;
import com.example.SpringBootTurialVip.dto.response.ProductOrderResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.Cart;
import com.example.SpringBootTurialVip.dto.request.OrderRequest;
import com.example.SpringBootTurialVip.entity.ProductOrder;
import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.enums.OrderStatus;
import com.example.SpringBootTurialVip.repository.UserRepository;
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
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private ProductOrder productOrder;

    //APi sp cho xem giỏ hàng
    //API hiển thị thông tin cá nhân để truy xuất giỏ hàng và ... - OK
    //@Operation(summary = "API hiển thị profile")
    private UserResponse getLoggedInUserDetails() {
        UserResponse user = userService.getMyInfo();
        return user;
    }

    //API cập nhật tình trạng đơn hàng
    @PreAuthorize("hasAnyRole('STAFF')")
    @Operation(summary = "API cập nhật trạng thái đơn hàng = id đơn hàng(STAFF)",description =
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
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @Operation(summary = "API trả về danh sách sản phẩm trong bước thanh toán , chỉ khác /cart cách format , xài nào cũng ĐƯỢC (CUSTOMER)")
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
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    @Operation(summary = "API này sẽ nhận cartId và tiến hành đặt hàng lưu vào db(CUSTOMER)")
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

    //API lưu đơn hàng = userid
    @PreAuthorize("hasAnyRole('STAFF','TEST')")
    @Operation(summary = "API nhận customerId và danh sách vaccine, " +
            "tiến hành lưu đơn hàng vào DB (CUSTOMER)")
    @PostMapping("/saveOrderByStaff")
    public ResponseEntity<ApiResponse<String>> saveOrderByStaff(
            @RequestParam Long customerId,
            @RequestParam OrderRequest orderRequest,
            @RequestBody ProductOrder productOrder) {
        try {
            // Kiểm tra khách hàng có tồn tại không
            User customer = userRepository.findById(customerId)
                    .orElseThrow(() -> new NoSuchElementException("Customer ID not found"));

            // Lưu đơn hàng vào DB
            orderService.saveOrderByStaff(customerId,productOrder,orderRequest);

            return ResponseEntity.ok(new ApiResponse<>(1000, "Order saved successfully", null));

        } catch (NoSuchElementException e) {
            log.error("Customer không tìm thấy: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(1004, "Error: " + e.getMessage(), null));

        } catch (IllegalStateException e) {
            log.error("Lỗi xử lý đơn hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(1004, "Error: " + e.getMessage(), null));

        } catch (Exception e) {
            log.error("Lỗi không xác định khi đặt hàng: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(1004, "Unexpected error: " + e.getMessage(), null));
        }
    }



    //API xem đơn hàng đã đặt
    @PreAuthorize("hasAnyRole('CUSTOMER')")
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

    //Xem đơn hàng của khách (STAFF)
    @PreAuthorize("hasAnyRole('STAFF')")
    @Operation(summary = "API lấy danh sách tất cả đơn hàng (STAFF)", description = "Trả về danh sách tất cả đơn hàng trong hệ thống.")
    @GetMapping("/all-orders")
    public ResponseEntity<ApiResponse<List<ProductOrderResponse>>> getAllOrders() {
        // Lấy danh sách tất cả đơn hàng từ service
        List<ProductOrder> orders = orderService.getAllOrders();

        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(1001, "No orders found", null));
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

        return ResponseEntity.ok(new ApiResponse<>(1000, "All orders retrieved successfully", orderResponses));
    }

    @PreAuthorize("hasAnyRole('STAFF', 'CUSTOMER','ADMIN')")
    @Operation(summary = "API tìm kiếm đơn hàng theo ID", description = "Trả về thông tin đơn hàng theo ID.")
    @GetMapping("/order/{id}")
    public ResponseEntity<ApiResponse<ProductOrderResponse>> getOrderById(
            @PathVariable Long id) {

        try {
            ProductOrder order = orderService.getOrderById(id);

            // Chuyển đổi sang DTO để ẩn thông tin nhạy cảm
            ProductOrderResponse orderResponse = new ProductOrderResponse(
                    order.getOrderId(),
                    order.getOrderDate(),
                    order.getProduct(),
                    order.getPrice(),
                    order.getQuantity(),
                    order.getStatus(),
                    order.getPaymentType(),
                    order.getOrderDetail()
            );

            return ResponseEntity.ok(new ApiResponse<>(1000, "Order found", orderResponse));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(1004, "Order not found", null));
        }
    }

    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @Operation(summary = "API đặt hàng bằng productId", description = "Tạo đơn hàng mới trực tiếp từ ID sản phẩm.")
    @PostMapping("/create-by-product")
    public ResponseEntity<ApiResponse<ProductOrder>> createOrderByProduct(
            @RequestParam Long productId,
            @RequestParam int quantity,
            @RequestBody OrderRequest orderRequest) {

        ProductOrder order = orderService.createOrderByProductId(productId, quantity, orderRequest);
        return ResponseEntity.ok(new ApiResponse<>(1000, "Order placed successfully", order));
    }

    //Xem danh sách đơn hàng = status
    @Operation(summary = "Lấy danh sách đơn hàng theo trạng thái",
            description = "Trả về danh sách đơn hàng dựa trên trạng thái được cung cấp")
    @GetMapping("/by-status")
    public List<ProductOrder> getOrdersByStatus(@RequestParam String status) {
        return orderService.getOrdersByStatus(status);
    }

    //Danh sách đơn hàng = status id
//    @Operation(summary = "Lấy danh sách đơn hàng theo mã trạng thái",
//            description = "Trả về danh sách đơn hàng dựa trên mã trạng thái được cung cấp")
//    @GetMapping("/by-status-id")
//    public List<ProductOrder> getOrdersByStatusId(@RequestParam Integer statusId) {
//        return orderService.getOrdersByStatusId(statusId);
//    }










}
