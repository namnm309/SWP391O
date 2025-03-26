package com.example.SpringBootTurialVip.controller.NewFormat;

import com.example.SpringBootTurialVip.dto.request.ApiResponse;
import com.example.SpringBootTurialVip.dto.response.OrderDetailResponse;
import com.example.SpringBootTurialVip.dto.response.ProductOrderResponse;
import com.example.SpringBootTurialVip.dto.response.ProductSuggestionResponse;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
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


    private UserResponse getLoggedInUserDetails() {
        UserResponse user = userService.getMyInfo();
        return user;
    }

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
                        detail.getStatus().getName(),
                        detail.getChild().getFullname(),
                        detail.getChild().getId()

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
                                    detail.getMobileNo(),
                                    detail.getChild()
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
                            detail.getStatus().getName(),
                            detail.getChild().getFullname(),
                            detail.getChild().getId()
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


    @PreAuthorize("hasAnyRole('CUSTOMER','STAFF','ADMIN')")
    @Operation(summary = "API đặt hàng - mỗi trẻ có thể đặt nhiều vaccine",
            description = "Tạo đơn hàng tiêm chủng với cấu trúc map childId → danh sách productId")
    @PostMapping("/create-by-product")
    public ResponseEntity<ApiResponse<ProductOrder>> createOrderByProductId(
            @RequestBody OrderRequest orderRequest) {

        Map<Long, List<Long>> childProductMap = orderRequest.getChildProductMap();

        if (childProductMap == null || childProductMap.isEmpty()) {
            throw new IllegalArgumentException("Danh sách trẻ và sản phẩm không được để trống.");
        }

        List<Long> allProductIds = childProductMap.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        List<Long> invalidProductIds = productService.findInvalidProductIds(allProductIds);
        if (!invalidProductIds.isEmpty()) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại với ID: " + invalidProductIds);
        }

        ProductOrder order = orderService.createOrderByChildProductMap(childProductMap, orderRequest);

        return ResponseEntity.ok(new ApiResponse<>(
                1000,
                "Đặt lịch thành công. Chúng tôi sẽ thông báo qua email trong thời gian sớm nhất.",
                order
        ));
    }





    @PreAuthorize("hasRole('STAFF')")
    @Operation(summary = "STAFF tạo đơn hàng cho nhiều trẻ", description = "Tạo đơn hàng với nhiều trẻ, mỗi trẻ nhiều vaccine.")
    @PostMapping("/staff/create-by-product")
    public ResponseEntity<ApiResponse<ProductOrder>> createOrderByStaff(
            @RequestBody OrderRequest orderRequest) {

        Map<Long, List<Long>> childProductMap = orderRequest.getChildProductMap();

        if (childProductMap == null || childProductMap.isEmpty()) {
            throw new IllegalArgumentException("Danh sách trẻ và sản phẩm không được để trống.");
        }

        List<Long> allProductIds = childProductMap.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        List<Long> invalidProductIds = productService.findInvalidProductIds(allProductIds);
        if (!invalidProductIds.isEmpty()) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại với ID: " + invalidProductIds);
        }

        ProductOrder order = orderService.createOrderByStaff(childProductMap, orderRequest);

        return ResponseEntity.ok(new ApiResponse<>(
                1000,
                "Đặt lịch thành công cho trẻ. Thông tin sẽ được cập nhật vào hệ thống.",
                order
        ));
    }



    //Xem danh sách đơn hàng = status
//    @Operation(summary = "BUG Lấy danh sách đơn hàng theo trạng thái(xem cơ bản)",
//            description = "Trả về danh sách đơn hàng dựa trên trạng thái được cung cấp")
//    @GetMapping("/by-status")
//    public List<ProductOrder> getOrdersByStatus(@RequestParam OrderDetailStatus status) {
//        return orderService.getOrdersByStatus(String.valueOf(status));
//    }

    //Danh sách đơn hàng = status id
//    @Operation(summary = "Lấy danh sách đơn hàng theo mã trạng thái",
//            description = "Trả về danh sách đơn hàng dựa trên mã trạng thái được cung cấp")
//    @GetMapping("/by-status-id")
//    public List<ProductOrder> getOrdersByStatusId(@RequestParam Integer statusId) {
//        return orderService.getOrdersByStatusId(statusId);
//    }

    //API xem lịch tiêm spa81 tới cho staff
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @Operation(
            summary = "Lấy danh sách lịch tiêm sắp tới cho STAFF",
            description = "API này cho phép STAFF xem toàn bộ lịch tiêm sắp tới. Có thể lọc theo ngày (date) và trạng thái (status). Trả về thông tin các OrderDetail chưa tiêm hoặc đã lên lịch."
    )
    //@GetMapping("/staff/schedule/upcoming")
    public ResponseEntity<ApiResponse<List<OrderDetailResponse>>> getUpcomingSchedules(
            @Parameter(
                    description = "Ngày muốn xem lịch tiêm (định dạng yyyy-MM-dd)",
                    example = "2025-03-25"
            )
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,

            @Parameter(
                    description = "Trạng thái cần lọc (CHUA_TIEM, DA_LEN_LICH, DA_TIEM, QUA_HAN)",
                    example = "DA_LEN_LICH"
            )
            @RequestParam(required = false)
            OrderDetailStatus status
    ) {
        List<OrderDetailResponse> result = orderService.getUpcomingSchedules(date, status);
        return ResponseEntity.ok(new ApiResponse<>(1000, "Danh sách lịch tiêm sắp tới", result));
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @Operation(
            summary = "Xem lịch tiêm theo tuần",
            description = "Trả về danh sách OrderDetail của các mũi tiêm trong 1 tuần cụ thể. Mặc định là tuần hiện tại nếu không truyền ngày."
    )
    @GetMapping("/staff/schedule/weekly")
    public ResponseEntity<ApiResponse<List<OrderDetailResponse>>> getWeeklySchedule(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Ngày bất kỳ trong tuần cần lấy lịch (format: yyyy-MM-dd)", example = "2025-03-24")
            LocalDate startDate
    ) {
        List<OrderDetailResponse> result = orderService.getWeeklySchedule(startDate);
        return ResponseEntity.ok(new ApiResponse<>(1000, "Lịch tiêm theo tuần", result));
    }

    //Xem lịch sắp tới ko status
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @Operation(
            summary = "Lấy danh sách lịch tiêm theo ngày (không cần trạng thái)",
            description = "Cho phép STAFF xem các mũi tiêm theo ngày. Nếu không truyền ngày thì trả toàn bộ danh sách."
    )
    @GetMapping("/staff/schedule/by-date")
    public ResponseEntity<ApiResponse<List<OrderDetailResponse>>> getUpcomingSchedulesWithoutStatus(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Ngày cần lọc lịch tiêm", example = "2025-03-26")
            LocalDate date
    ) {
        List<OrderDetailResponse> result = orderService.getUpcomingSchedulesWithoutStatus(date);
        return ResponseEntity.ok(new ApiResponse<>(1000, "Danh sách lịch tiêm theo ngày", result));
    }

//    @PreAuthorize("hasAnyRole('CUSTOMER','STAFF','ADMIN')")
//    @Operation(summary = "Tư vấn vaccine phù hợp cho trẻ", description = "Gợi ý danh sách vaccine theo độ tuổi và số mũi còn lại")
//    @GetMapping("/vaccine/suggestion")
//    public ResponseEntity<ApiResponse<List<Product>>> suggestVaccines(
//            @RequestParam Long childId) {
//        List<Product> result = orderService.suggestVaccinesForChild(childId);
//        return ResponseEntity.ok(new ApiResponse<>(1000, "Gợi ý vaccine thành công", result));
//    }

@PreAuthorize("hasAnyRole('CUSTOMER','STAFF','ADMIN')")
@GetMapping("/vaccine/suggestion")
public ResponseEntity<ApiResponse<List<ProductSuggestionResponse>>> suggestVaccines(
        @RequestParam Long childId) {
    List<ProductSuggestionResponse> result = orderService.suggestVaccinesForChild(childId);
    return ResponseEntity.ok(new ApiResponse<>(1000, "Gợi ý vaccine thành công", result));
}


    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @Operation(summary = "Gợi ý vaccine phù hợp cho staff (bỏ qua kiểm tra cha mẹ)")
    @GetMapping("/vaccine/suggestion/staff")
    public ResponseEntity<ApiResponse<List<ProductSuggestionResponse>>> suggestVaccinesForStaff(
            @RequestParam Long childId) {

        List<ProductSuggestionResponse> result = orderService.suggestVaccinesByStaff(childId);
        return ResponseEntity.ok(new ApiResponse<>(1000, "Gợi ý vaccine thành công", result));
    }


    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    @PutMapping("/cancel-order/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable String orderId) throws AccessDeniedException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");

        orderService.cancelOrderByCustomer(orderId, userId);
        return ResponseEntity.ok(new ApiResponse<>(1000, "Hủy đơn hàng thành công", null));
    }



    @PreAuthorize("hasAnyRole('STAFF', 'ADMIN')")
    @PutMapping("/cancel-order-by-staff/{orderId}")
    public ResponseEntity<?> cancelOrderByStaff(
            @PathVariable String orderId,
            @RequestParam(required = false) String reason) {

        orderService.cancelOrderByStaff(orderId, reason);
        return ResponseEntity.ok(new ApiResponse<>(1000, "Hủy đơn hàng thành công bởi nhân viên", null));
    }

    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    @Operation(summary = "Xem lịch tiêm chủng sắp tới cho staff", description = "Lấy danh sách các mũi tiêm đã lên lịch theo ngày hoặc tuần.")
    @GetMapping("/staff/upcoming-schedules")
    public ResponseEntity<ApiResponse<List<OrderDetailResponse>>> getUpcomingSchedulesForStaff(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) OrderDetailStatus status) {

        // Nếu không truyền ngày, mặc định là hôm nay
        if (fromDate == null) {
            fromDate = LocalDateTime.now();
        }

        List<OrderDetailResponse> list = orderService.getUpcomingSchedulesForStaff(fromDate, status);
        return ResponseEntity.ok(new ApiResponse<>(1000, "Lịch tiêm sắp tới", list));
    }




















}
