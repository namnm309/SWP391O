package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.response.OrderDetailResponse;
import com.example.SpringBootTurialVip.dto.response.ProductOrderResponse;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.dto.request.OrderRequest;
import com.example.SpringBootTurialVip.entity.OrderDetail;
import com.example.SpringBootTurialVip.entity.ProductOrder;
import com.example.SpringBootTurialVip.enums.OrderDetailStatus;
import com.example.SpringBootTurialVip.repository.OrderDetailRepository;
import com.example.SpringBootTurialVip.repository.ProductOrderRepository;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.*;
import com.example.SpringBootTurialVip.service.serviceimpl.UserService;
import com.example.SpringBootTurialVip.util.CommonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
@Tag(name="[Order]",description = "")
@PreAuthorize("hasAnyRole('CUSTOMER','STAFF', 'ADMIN')")
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

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProductService productService;

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
//    @PreAuthorize("hasAnyRole('STAFF')")
//    @Operation(summary = "API cập nhật trạng thái đơn hàng = id đơn hàng(STAFF)",description =
//            "StatusID list : (1,In Progress) \n"+
//                    "(2,Order Received) \n" +
//                    "(3, Out for Stock) \n" +
//                    "(4,Cancelled) \n" +
//                    "(5,Success) \n")
//    @PutMapping("/update-status")
//    public ResponseEntity<ApiResponse<ProductOrder>> updateOrderStatus(
//            @RequestParam Long id,//ID đơn hàng
//            @RequestParam Integer statusId) {
//
//        //Tìm `OrderStatus` nhanh hơn bằng Stream API
//        Optional<OrderStatus> matchedStatus = Arrays.stream(OrderStatus.values())
//                .filter(orderStatus -> orderStatus.getId().equals(statusId))
//                .findFirst();
//
//        if (matchedStatus.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ApiResponse<>(1001, "Invalid status ID", null));
//        }
//
//        String status = matchedStatus.get().getName();
//
//        // Cập nhật trạng thái đơn hàng
//        ProductOrder updatedOrder = orderService.updateOrderStatus(id, status);
//
//        if (updatedOrder == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(1002, "Order not found or status not updated", null));
//        }
//
//        // Gửi email thông báo nếu cần
//        try {
//            commonUtil.sendMailForProductOrder(updatedOrder, status);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(1003, "Order updated but email failed", updatedOrder));
//        }
//
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Order status updated successfully", updatedOrder));
//    }

    //Api cập nhật tt đơn hàng v2
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
//    @Operation(summary = "API cập nhật trạng thái đơn hàngV2", description = "Cập nhật trạng thái đơn hàng theo order_id.")
//    @PutMapping("/update-status-v2")
//    public ResponseEntity<ApiResponse<ProductOrder>> updateOrderStatusv2(
//            @RequestParam String orderId, // ID đơn hàng
//            @Parameter(description = "Trạng thái đơn hàng", schema = @Schema(implementation = OrderStatus.class))
//            @RequestParam OrderStatus status // Dùng Enum trực tiếp thay vì số ID
//    ) {
//        // Tìm đơn hàng theo orderId
//        ProductOrder order = orderService.getOrderByOrderId(orderId);
//        if (order == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(1002, "Order not found", null));
//        }
//
//        // Cập nhật trạng thái đơn hàng
//        order.setStatus(status.getName()); // Lấy tên trạng thái từ Enum
//        productOrderRepository.save(order);
//
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Order status updated successfully", order));
//    }

//    @Operation(summary = "Cập nhật trạng thái OrderDetail , đủ sl DA_TIEM thì status của productorder auto = SUCCESS",
//            description = "Cập nhật trạng thái của một OrderDetail và kiểm tra xem có cần cập nhật ProductOrder không.")
//    @PutMapping("/{id}/status")
//    public ResponseEntity<String> updateOrderDetailStatus(
//            @Parameter(description = "ID của OrderDetail", example = "12345")
//            @PathVariable Long id,
//            @Parameter(description = "Trạng thái mới của OrderDetail", example = "DA_TIEM")
//            @RequestParam OrderDetailStatus status) {
//
//        orderService.updateOrderDetailStatus(id, status);
//        return ResponseEntity.ok("Order detail status updated successfully!");
//    }

    //API v2
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
//    @Operation(summary = "Cập nhật ngày tiêm cho sản phẩm trong đơn hàng", description = "Cập nhật ngày tiêm dựa trên orderDetailId.")
//    @PutMapping("/update-vaccination-date")
//    public ResponseEntity<ApiResponse<OrderDetail>> updateVaccinationDate(
//            @RequestParam Long orderDetailId, // ID của chi tiết đơn hàng
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate vaccinationDate // Ngày tiêm chủng mới
//    ) {
//        // Lấy orderDetail theo ID
//        OrderDetail orderDetail = orderDetailRepository.findById(Math.toIntExact(orderDetailId))
//                .orElseThrow(() -> new NoSuchElementException("OrderDetail not found with ID: " + orderDetailId));
//
//        // Kiểm tra nếu ngày tiêm nhỏ hơn ngày hiện tại
//        if (vaccinationDate.isBefore(LocalDate.now())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ApiResponse<>(1001, "Vaccination date cannot be in the past", null));
//        }
//
//        // Cập nhật ngày tiêm
//        orderDetail.setVaccinationDate(vaccinationDate);
//        orderDetailRepository.save(orderDetail);
//
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Vaccination date updated successfully", orderDetail));
//    }

    //APi cập nhật ngày tiêm có gửi mail
//    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
//    @Operation(summary = "API cập nhật ngày tiêm cho OrderDetail(có mail)")
//    @PutMapping("/update-vaccination-date-mail")
//    public ResponseEntity<ApiResponse<OrderDetailResponse>> updateVaccinationDateMail(
//            @RequestParam Integer orderDetailId,
//            @Parameter(
//                    description = "Ngày giờ tiêm chủng mới (Định dạng: yyyy-MM-dd'T'HH:mm:ss)",
//                    example = "2025-03-20T14:30:00",
//                    required = true
//            )
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime vaccinationDate) {
//
//        // Tìm OrderDetail cần cập nhật
//        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
//                .orElseThrow(() -> new NoSuchElementException("OrderDetail not found with ID: " + orderDetailId));
//
//        // Cập nhật ngày tiêm
//        orderDetail.setVaccinationDate(vaccinationDate);
//        orderDetailRepository.save(orderDetail);
//
//        // Gửi email thông báo cho khách hàng
//        emailService.sendVaccinationUpdateEmail(orderDetail);
//
//        // Gửi notification cho khách hàng (dùng method có sẵn của bạn)
//        notificationService.sendNotification(orderDetail.getChild().getParentid(),
//                "Lịch tiêm chủng của bạn cho vaccine " + orderDetail.getProduct().getTitle()
//                        + " đã được cập nhật vào ngày " + vaccinationDate);
//
//        // Tạo response
//        OrderDetailResponse response = new OrderDetailResponse(
//                orderDetail.getId(),
//                orderDetail.getProduct().getTitle(),
//                orderDetail.getQuantity(),
//                orderDetail.getVaccinationDate(),
//                orderDetail.getProduct().getDiscountPrice(),
//                orderDetail.getFirstName(),
//                orderDetail.getLastName(),
//                orderDetail.getEmail(),
//                orderDetail.getMobileNo()
//        );
//
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Vaccination date updated successfully", response));
//    }
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @Operation(summary = "Cập nhật ngày tiêm cho OrderDetail (có gửi mail)")
    @PutMapping("/update-vaccination-date-mail")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> updateVaccinationDateMail(
            @RequestParam Long orderDetailId,
            @Parameter(
                    description = "Ngày giờ tiêm chủng mới (Định dạng: yyyy-MM-dd'T'HH:mm:ss)",
                    example = "2025-03-20T14:30:00",
                    required = true
            ) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime vaccinationDate) {

        // Cập nhật lịch tiêm
        OrderDetail updatedOrderDetail = orderService.updateVaccinationDate(orderDetailId, vaccinationDate);

        // Tạo response
        OrderDetailResponse response = new OrderDetailResponse(
                updatedOrderDetail.getId(),
                updatedOrderDetail.getProduct().getTitle(),
                updatedOrderDetail.getQuantity(),
                updatedOrderDetail.getVaccinationDate(),
                updatedOrderDetail.getProduct().getDiscountPrice(),
                updatedOrderDetail.getFirstName(),
                updatedOrderDetail.getLastName(),
                updatedOrderDetail.getEmail(),
                updatedOrderDetail.getMobileNo()
        );

        return ResponseEntity.ok(new ApiResponse<>(1000, "Lịch tiêm đã cập nhật thành công", response));
    }


    //API cập nhật trạng thái đã tiêm
    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateOrderDetailStatus(
            @PathVariable Long id,
            @RequestParam OrderDetailStatus status) {

        orderService.updateOrderDetailStatus(id, status);
        return ResponseEntity.ok("Order detail status updated successfully!");
    }



    //API show list orderdetail thoe order_id
    @PreAuthorize("hasAnyRole('CUSTOMER','STAFF', 'ADMIN')")
    @Operation(summary = "Lấy danh sách OrderDetail theo Order ID",
            description = "Trả về danh sách tất cả OrderDetail của một đơn hàng dựa trên orderId.")
    @GetMapping("/order-details/{orderId}")
    public ResponseEntity<ApiResponse<List<OrderDetailResponse>>> getOrderDetailsByOrderId(@PathVariable String orderId) {

        // Lấy danh sách OrderDetail theo orderId
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);

        if (orderDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(1001, "No order details found for orderId: " + orderId, null));
        }

        // Chuyển đổi danh sách OrderDetail sang OrderDetailResponse
        List<OrderDetailResponse> orderDetailResponses = orderDetails.stream()
                .map( detail -> new OrderDetailResponse(
                        detail.getId(), // orderDetailId
                        detail.getProduct().getTitle(), // Tên sản phẩm
                        detail.getQuantity(), // Số lượng
                        detail.getOrderId(),
                        detail.getVaccinationDate(), // Ngày tiêm
                        detail.getProduct().getDiscountPrice(), // Giá
                        detail.getFirstName(),
                        detail.getLastName(),
                        detail.getEmail(),
                        detail.getMobileNo(),
                        detail.getStatus().getName()

                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(1000, "Order details retrieved successfully", orderDetailResponses));
    }

    //API xem đơn hàng đã đặt
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @Operation(summary = "API xem đơn hàng đã đặt(xem chi tiết)")
    @GetMapping("/user-orders")
    public ResponseEntity<ApiResponse<List<ProductOrderResponse>>> myOrder() {
        UserResponse loginUser = getLoggedInUserDetails();

        if (loginUser == null || loginUser.getId() == null) {
            log.error("Failed to retrieve logged-in user or user ID is null.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(1001, "Unauthorized: Cannot retrieve user", null));
        }

        List<ProductOrder> orders = orderService.getOrdersByUser(loginUser.getId());

        // Chuyển đổi danh sách `ProductOrder` sang `ProductOrderResponse`
        List<ProductOrderResponse> orderResponses = orders.stream()
                .map(order -> {
                    // Lấy danh sách OrderDetail tương ứng
                    List<OrderDetailResponse> orderDetails = orderDetailRepository.findByOrderId(order.getOrderId()).stream()
                            .map(detail -> new OrderDetailResponse(
                                    detail.getId(),
                                    detail.getProduct().getTitle(),
                                    detail.getQuantity(),
                                    detail.getOrderId(),
                                    detail.getVaccinationDate(),
                                    detail.getProduct().getDiscountPrice(),
                                    detail.getFirstName(),
                                    detail.getLastName(),
                                    detail.getEmail(),
                                    detail.getMobileNo()
                            ))
                            .collect(Collectors.toList());

                    // Tạo ProductOrderResponse
                    return new ProductOrderResponse(
                            order.getOrderId(),
                            order.getOrderDate(),
                            order.getStatus(),
                            order.getPaymentType(),
                            order.getTotalPrice(),
                            orderDetails // Gán danh sách OrderDetailResponse
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(1000, "User orders retrieved successfully", orderResponses));
    }


    //Xem đơn hàng của khách (STAFF)
    @PreAuthorize("hasAnyRole('STAFF','ADMIN','ROLE_ROLE_STAFF')")
    @Operation(summary = "API lấy danh sách tất cả đơn hàng (STAFF)(xem cơ bản)", description = "Trả về danh sách tất cả đơn hàng trong hệ thống.")
    @GetMapping("/all-orders")
    public ResponseEntity<ApiResponse<List<ProductOrderResponse>>> getAllOrders() {
        // Lấy danh sách tất cả đơn hàng từ service
        List<ProductOrder> orders = orderService.getAllOrders();

        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(1001, "No orders found", null));
        }

        // Chuyển đổi danh sách `ProductOrder` sang `ProductOrderResponse` nhưng không chứa OrderDetailResponse
        List<ProductOrderResponse> orderResponses = orders.stream()
                .map(order -> new ProductOrderResponse(
                        order.getOrderId(),
                        order.getOrderDate(),
                        order.getStatus(),
                        order.getPaymentType(),
                        order.getTotalPrice(),
                        null // Không hiển thị danh sách OrderDetailResponse
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(1000, "All orders retrieved successfully", orderResponses));
    }


    @PreAuthorize("hasAnyRole('STAFF', 'CUSTOMER','ADMIN')")
    @Operation(summary = "API tìm kiếm đơn hàng theo Order ID(xem chi tiết)", description = "Trả về thông tin đơn hàng theo Order ID.")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<ProductOrderResponse>> getOrderById(@PathVariable String orderId) {

        try {
            // Tìm kiếm đơn hàng bằng orderId
            ProductOrder order = orderService.getOrderByOrderId(orderId);

            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(1004, "Order not found", null));
            }

            // Lấy danh sách OrderDetail tương ứng
            List<OrderDetailResponse> orderDetails = orderDetailRepository.findByOrderId(orderId).stream()
                    .map(detail -> new OrderDetailResponse(
                            detail.getId(),
                            detail.getProduct().getTitle(),
                            detail.getQuantity(),
                            detail.getOrderId(),
                            detail.getVaccinationDate(),
                            detail.getProduct().getDiscountPrice(),
                            detail.getFirstName(),
                            detail.getLastName(),
                            detail.getEmail(),
                            detail.getMobileNo(),
                            detail.getStatus().getName()
                    ))
                    .collect(Collectors.toList());

            // Chuyển đổi sang DTO
            ProductOrderResponse orderResponse = new ProductOrderResponse(
                    order.getOrderId(),
                    order.getOrderDate(),
                    order.getStatus(),
                    order.getPaymentType(),
                    order.getTotalPrice(),
                    orderDetails // Gán danh sách OrderDetailResponse
            );

            return ResponseEntity.ok(new ApiResponse<>(1000, "Order found", orderResponse));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(1004, "Order not found", null));
        }
    }


    @PreAuthorize("hasAnyRole('CUSTOMER','STAFF', 'ADMIN')")
    @Operation(summary = "API đặt hàng bằng productId cho customer", description = "Tạo đơn hàng mới trực tiếp từ ID sản phẩm.")
//    @PostMapping("/create-by-product")
//    public ResponseEntity<ApiResponse<ProductOrder>> createOrderByProduct(
//            @RequestParam List<Long> productId,
//            @RequestParam List<Integer> quantity,
//            @RequestBody OrderRequest orderRequest) {
//
//        if (productId == null || quantity == null || productId.isEmpty() || quantity.isEmpty()) {
//            throw new IllegalArgumentException("Mã sản phẩm và số lượng không thể bỏ trống !Vui lòng thử lại .");
//        }
//
//        if (productId.size() != quantity.size()) {
//            throw new IllegalArgumentException("Mỗi sản phẩm phải có số lượng tương .");
//        }
//
//        ProductOrder order = orderService.createOrderByProductId(productId, quantity, orderRequest);
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Order placed successfully", order));
//    }
    @PostMapping("/create-by-product")
    public ResponseEntity<ApiResponse<ProductOrder>> createOrderByProductId(
            @RequestParam List<Long> productId,
            @RequestBody OrderRequest orderRequest) {

        // Kiểm tra productId không null
        if (productId == null || productId.isEmpty()) {
            throw new IllegalArgumentException("Danh sách sản phẩm không được để trống.");
        }

        // Kiểm tra productId hợp lệ
        List<Long> invalidProductIds = productService.findInvalidProductIds(productId);
        if (!invalidProductIds.isEmpty()) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại với ID: " + invalidProductIds);
        }

        // Gọi service mới xử lý tự động
        ProductOrder order = orderService.createOrderByProductId(productId, orderRequest);

        return ResponseEntity.ok(new ApiResponse<>(
                1000,
                "Đặt lịch thành công. Chúng tôi sẽ thông báo tới quý khách qua email trong thời gian sớm nhất. Xin cảm ơn.",
                order
        ));
    }


    @PreAuthorize("hasAnyRole('CUSTOMER','STAFF', 'ADMIN')")
    @Operation(summary = "API đặt hàng tự động cho staff", description = "Tạo đơn hàng mới từ sản phẩm, tự tính số mũi còn lại.")
    @PostMapping("/create-auto-by-staff")
    public ResponseEntity<ApiResponse<ProductOrder>> createOrderAutoByStaff(
            @RequestParam Long userId,
            @RequestParam List<Long> productId,
            @RequestBody OrderRequest orderRequest) {

        // Kiểm tra danh sách sản phẩm có rỗng không
        if (productId == null || productId.isEmpty()) {
            throw new IllegalArgumentException("Danh sách sản phẩm không được để trống.");
        }

        // Kiểm tra productId có tồn tại trong DB không
        List<Long> invalidProductIds = productService.findInvalidProductIds(productId);
        if (!invalidProductIds.isEmpty()) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại với ID: " + invalidProductIds);
        }

        // Bỏ kiểm tra tồn kho ở đây — vì đã xử lý trong service (dựa vào số mũi cần đặt thực tế)
        // Gọi service xử lý nghiệp vụ
        ProductOrder order = orderService.createOrderByProductIdByStaff(userId, productId, orderRequest);

        return ResponseEntity.ok(
                new ApiResponse<>(1000, "Đặt lịch thành công. Chúng tôi sẽ thông báo tới quý khách qua email trong thời gian sớm nhất. Xin cảm ơn.", order)
        );
    }


    //Xem danh sách đơn hàng = status
    @Operation(summary = "Lấy danh sách đơn hàng theo trạng thái(xem cơ bản)",
            description = "Trả về danh sách đơn hàng dựa trên trạng thái được cung cấp")
    @GetMapping("/by-status")
    public List<ProductOrder> getOrdersByStatus(@RequestParam OrderDetailStatus status) {
        return orderService.getOrdersByStatus(String.valueOf(status));
    }

    //Danh sách đơn hàng = status id
//    @Operation(summary = "Lấy danh sách đơn hàng theo mã trạng thái",
//            description = "Trả về danh sách đơn hàng dựa trên mã trạng thái được cung cấp")
//    @GetMapping("/by-status-id")
//    public List<ProductOrder> getOrdersByStatusId(@RequestParam Integer statusId) {
//        return orderService.getOrdersByStatusId(statusId);
//    }










}
