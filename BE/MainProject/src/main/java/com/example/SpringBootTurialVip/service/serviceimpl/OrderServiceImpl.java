package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.OrderRequest;
import com.example.SpringBootTurialVip.dto.response.*;
import com.example.SpringBootTurialVip.entity.*;
import com.example.SpringBootTurialVip.enums.AgeGroup;
import com.example.SpringBootTurialVip.enums.OrderDetailStatus;
import com.example.SpringBootTurialVip.repository.*;
import com.example.SpringBootTurialVip.service.EmailService;
import com.example.SpringBootTurialVip.service.NotificationService;
import com.example.SpringBootTurialVip.service.OrderService;
import com.example.SpringBootTurialVip.util.CommonUtil;
import com.example.SpringBootTurialVip.enums.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.SpringBootTurialVip.enums.OrderStatus.CANCEL;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductOrderRepository orderRepository;

    @Autowired
    private CommonUtil commonUtil;

//    @Autowired
//    private CartRepository cartRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private ProductDetailsRepository productDetailsRepository;



    @Override
    public List<ProductOrder> getOrdersByUser(Long userId) {
        List<ProductOrder> orders = orderRepository.findByUserId(userId);
        return orders;
    }

//    @Override
//    public ProductOrder updateOrderStatus(Long id, String status) {
//        Optional<ProductOrder> findById = orderRepository.findById(id);
//        if (findById.isPresent()) {
//            ProductOrder productOrder = findById.get();
//            productOrder.setStatus(status);
//            ProductOrder updateOrder = orderRepository.save(productOrder);
//            // Gửi thông báo khi trạng thái đơn vaccine thay đổi
//            notificationService.sendOrderStatusNotification(productOrder.getUser().getId(), status);
//            return updateOrder;
//        }
//        return null;
//    }

    @Override
    public List<ProductOrder> getAllOrders() {
        return productOrderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
    }



    @Override
    public List<VaccineOrderStats> getTopVaccines(int month, int year) {
        return productOrderRepository.findTopVaccinesByMonthAndYear(month, year);
    }
//
    @Override
    public List<VaccineOrderStats> getLeastOrderedVaccines(int month, int year) {
        return productOrderRepository.findLeastOrderedVaccines(month, year);
    }

    @Override
    public ProductOrder getOrderByOrderId(String orderId) {
        ProductOrder order = productOrderRepository.findByOrderId(orderId);
        if (order == null) {
            throw new NoSuchElementException("Order not found with orderId: " + orderId);
        }
        return order;
    }

    @Override
    public List<VaccinationHistoryResponse> getChildVaccinationHistory(Long childId) {
        List<VaccinationHistoryResponse> historyList = orderDetailRepository.getVaccinationHistory(childId);

        for (VaccinationHistoryResponse item : historyList) {
            Long orderDetailId = item.getOrderDetailId().longValue();
            Optional<Reaction> optionalReaction = reactionRepository.findByOrderDetailId(orderDetailId);

            optionalReaction.ifPresent(reaction -> {
                item.setReaction(new ReactionResponse(reaction));
            });
        }

        return historyList;
    }

    @Override
    public List<UpcomingVaccinationResponse> getUpcomingVaccinations(Long childId) {
        return orderDetailRepository.getUpcomingVaccinations(childId);
    }

    @Override
    public List<UpcomingVaccinationResponse> getUpcomingVaccinationsForAllChildren(Long parentId) {
        List<User> children = userRepository.findByParentid(parentId);
        List<UpcomingVaccinationResponse> upcomingVaccinations = new ArrayList<>();

        for (User child : children) {
            List<UpcomingVaccinationResponse> childVaccinations = getUpcomingVaccinations(child.getId());
            upcomingVaccinations.addAll(childVaccinations);
        }

        return upcomingVaccinations;
    }

//    @Override
//    @Transactional
//    public void updateOrderDetailStatus(Long orderDetailId, OrderDetailStatus newStatus) {
//        // 1. Lấy OrderDetail
//        OrderDetail detail = orderDetailRepository.findById(Math.toIntExact(orderDetailId))
//                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy OrderDetail với ID: " + orderDetailId));
//
//        // 2. Không cho đổi nếu đã DA_TIEM
//        if (detail.getStatus() == OrderDetailStatus.DA_TIEM) {
//            throw new IllegalStateException("OrderDetail đã ở trạng thái Đã tiêm, không thể thay đổi trạng thái!");
//        }
//
//        // 3. Gán trạng thái mới
//        detail.setStatus(newStatus);
//        orderDetailRepository.save(detail);
//
//        // 4. Lấy ProductOrder (để cập nhật OrderStatus & lấy user)
//        String orderId = detail.getOrderId();
//        ProductOrder order = productOrderRepository.findByOrderId(orderId);
//        if (order == null) {
//            throw new NoSuchElementException("Không tìm thấy ProductOrder với orderId: " + orderId);
//        }
//
//        // 5. Lấy userId từ đơn hàng (hoặc từ detail, tùy cấu trúc)
//        //   Giả sử: ProductOrder có quan hệ 1-n với OrderDetail, và ProductOrder có 1 trường `User user`
//        Long userId = order.getUser().getId();
//
//        // 6. Xử lý logic + thông báo theo từng trạng thái
//        switch (newStatus) {
//            case DA_LEN_LICH:
//                // Gửi email
//                emailService.sendVaccinationUpdateEmail(detail);
//                // Tạo message notify
//                notificationService.sendNotification(userId,
//                        "Chi tiết đơn hàng " + orderId + " đã lên lịch tiêm chủng. Vui lòng kiểm tra thông tin.");
//                break;
//
//            case DA_TIEM:
//                // Trừ tồn kho
//                Product product = detail.getProduct();
//                int qty = detail.getQuantity();
//                product.setQuantity(product.getQuantity() - qty);
//                product.setReservedQuantity(product.getReservedQuantity() - qty);
//                productRepository.save(product);
//
//                // Gửi email
//                emailService.sendCustomEmail(
//                        detail.getEmail(),
//                        "Xác nhận đã tiêm vaccine",
//                        "Chi tiết đơn hàng " + orderId + " đã được tiêm thành công."
//                );
//                // Gửi thông báo trong hệ thống
//                notificationService.sendNotification(userId,
//                        "Chi tiết đơn hàng " + orderId + " đã được tiêm thành công.");
//                break;
//
//            case DA_HUY:
//                // Gửi email
//                emailService.sendCustomEmail(
//                        detail.getEmail(),
//                        "Xác nhận hủy chi tiết tiêm",
//                        "Chi tiết đơn hàng " + orderId + " đã bị hủy."
//                );
//                // Gửi thông báo trong hệ thống
//                notificationService.sendNotification(userId,
//                        "Chi tiết đơn hàng " + orderId + " đã bị hủy.");
//                break;
//
//            // Nếu còn trạng thái khác thì ta xử lý tương tự
//            default:
//                // CHUA_TIEM, QUA_HAN,... tuỳ bạn
//                break;
//        }
//
//        // 7. Tổng hợp các OrderDetail để suy ra OrderStatus
//        List<OrderDetail> allDetails = orderDetailRepository.findByOrderId(orderId);
//
//        boolean allCanceled = allDetails.stream().allMatch(d -> d.getStatus() == OrderDetailStatus.DA_HUY);
//        boolean allDone = allDetails.stream().allMatch(d -> d.getStatus() == OrderDetailStatus.DA_TIEM);
//        boolean anyCanceled = allDetails.stream().anyMatch(d -> d.getStatus() == OrderDetailStatus.DA_HUY);
//
//        /*
//         *  Nếu toàn bộ chi tiết DA_HUY --> OrderStatus = CANCEL
//         *  Nếu toàn bộ chi tiết DA_TIEM --> OrderStatus = SUCCESS
//         *  Nếu có ít nhất một cái DA_HUY, còn lại chưa xong --> CANCELED_PARTIAL
//         *  Ngược lại (chưa tiêm xong, chưa hủy hết) --> IN_PROGRESS
//         */
//        if (allCanceled) {
//            order.setStatus(OrderStatus.CANCEL.getName());
//            // Ví dụ: gửi noti "Đơn hàng đã hủy toàn bộ"
//            notificationService.sendOrderStatusNotification(userId, "Đã hủy toàn bộ");
//        } else if (allDone) {
//            order.setStatus(OrderStatus.SUCCESS.getName());
//            // Gửi noti "Đơn hàng đã tiêm xong"
//            notificationService.sendOrderStatusNotification(userId, "Đã tiêm xong");
//        } else if (anyCanceled) {
//            order.setStatus(OrderStatus.CANCELED_PARTIAL.getName());
//            // Gửi noti "Đơn hàng bị hủy một phần"
//            notificationService.sendOrderStatusNotification(userId, "Đơn hàng bị hủy một phần");
//        } else {
//            // Mặc định = IN_PROGRESS
//            order.setStatus(OrderStatus.IN_PROGRESS.getName());
//            // Gửi noti "Đơn hàng đang xử lý"
//            notificationService.sendOrderStatusNotification(userId, "Đang xử lý");
//        }
//
//        // 8. Lưu lại Order
//        productOrderRepository.save(order);
//    }
//@Override
//@Transactional
//public void updateOrderDetailStatus(Long orderDetailId, OrderDetailStatus newStatus) {
//    // 1. Lấy OrderDetail
//    OrderDetail detail = orderDetailRepository.findById(Math.toIntExact(orderDetailId))
//            .orElseThrow(() -> new NoSuchElementException("Không tìm thấy OrderDetail với ID: " + orderDetailId));
//
//    // 2. Không cho đổi nếu đã DA_TIEM
//    if (detail.getStatus() == OrderDetailStatus.DA_TIEM) {
//        throw new IllegalStateException("OrderDetail đã ở trạng thái Đã tiêm, không thể thay đổi trạng thái!");
//    }
//
//    // 3. Gán trạng thái mới
//    detail.setStatus(newStatus);
//    orderDetailRepository.save(detail);
//
//    // 4. Lấy ProductOrder (để cập nhật OrderStatus & lấy user)
//    String orderId = detail.getOrderId();
//    ProductOrder order = productOrderRepository.findByOrderId(orderId);
//    if (order == null) {
//        throw new NoSuchElementException("Không tìm thấy ProductOrder với orderId: " + orderId);
//    }
//
//    // 5. Lấy userId từ đơn hàng (hoặc từ detail, tùy cấu trúc)
//    Long userId = order.getUser().getId();
//
//    // 6. Xử lý logic + thông báo theo từng trạng thái
//    switch (newStatus) {
//        case DA_LEN_LICH:
//            // Gửi email
//            emailService.sendVaccinationUpdateEmail(detail);
//            // Tạo message notify
//            notificationService.sendNotification(userId,
//                    "Chi tiết đơn hàng " + orderId + " đã lên lịch tiêm chủng. Vui lòng kiểm tra thông tin.");
//            break;
//
//        case DA_TIEM:
//            // Trừ tồn kho - Chỉ giảm số lượng trong 1 lô (ProductDetail)
//            Product product = detail.getProduct();
//            ProductDetails productDetail = detail.getProductDetails(); // Lấy ProductDetail (lô hàng) liên quan đến OrderDetail
//
//            if (productDetail != null) {
//                int qty = detail.getQuantity(); // Số lượng đã đặt
//                if (productDetail.getQuantity() - productDetail.getReservedQuantity() >= qty) {
//                    productDetail.updateReservedQuantity(qty); // Giảm số lượng trong ProductDetail
//                    product.setReservedQuantity(product.getReservedQuantity() + qty); // Cập nhật lại tổng reservedQuantity trong Product
//                    productRepository.save(product); // Lưu lại Product sau khi cập nhật
//                }
//            }
//
//            // Gửi email
//            emailService.sendCustomEmail(
//                    detail.getEmail(),
//                    "Xác nhận đã tiêm vaccine",
//                    "Chi tiết đơn hàng " + orderId + " đã được tiêm thành công."
//            );
//            // Gửi thông báo trong hệ thống
//            notificationService.sendNotification(userId,
//                    "Chi tiết đơn hàng " + orderId + " đã được tiêm thành công.");
//            break;
//
//        case DA_HUY:
//            // Gửi email
//            emailService.sendCustomEmail(
//                    detail.getEmail(),
//                    "Xác nhận hủy chi tiết tiêm",
//                    "Chi tiết đơn hàng " + orderId + " đã bị hủy."
//            );
//            // Gửi thông báo trong hệ thống
//            notificationService.sendNotification(userId,
//                    "Chi tiết đơn hàng " + orderId + " đã bị hủy.");
//            break;
//
//        // Nếu còn trạng thái khác thì ta xử lý tương tự
//        default:
//            // CHUA_TIEM, QUA_HAN,... tuỳ bạn
//            break;
//    }
//
//    // 7. Tổng hợp các OrderDetail để suy ra OrderStatus
//    List<OrderDetail> allDetails = orderDetailRepository.findByOrderId(orderId);
//
//    boolean allCanceled = allDetails.stream().allMatch(d -> d.getStatus() == OrderDetailStatus.DA_HUY);
//    boolean allDone = allDetails.stream().allMatch(d -> d.getStatus() == OrderDetailStatus.DA_TIEM);
//    boolean anyCanceled = allDetails.stream().anyMatch(d -> d.getStatus() == OrderDetailStatus.DA_HUY);
//
//    /*
//     *  Nếu toàn bộ chi tiết DA_HUY --> OrderStatus = CANCEL
//     *  Nếu toàn bộ chi tiết DA_TIEM --> OrderStatus = SUCCESS
//     *  Nếu có ít nhất một cái DA_HUY, còn lại chưa xong --> CANCELED_PARTIAL
//     *  Ngược lại (chưa tiêm xong, chưa hủy hết) --> IN_PROGRESS
//     */
//    if (allCanceled) {
//        order.setStatus(OrderStatus.CANCEL.getName());
//        // Ví dụ: gửi noti "Đơn hàng đã hủy toàn bộ"
//        notificationService.sendOrderStatusNotification(userId, "Đã hủy toàn bộ");
//    } else if (allDone) {
//        order.setStatus(OrderStatus.SUCCESS.getName());
//        // Gửi noti "Đơn hàng đã tiêm xong"
//        notificationService.sendOrderStatusNotification(userId, "Đã tiêm xong");
//    } else if (anyCanceled) {
//        order.setStatus(OrderStatus.CANCELED_PARTIAL.getName());
//        // Gửi noti "Đơn hàng bị hủy một phần"
//        notificationService.sendOrderStatusNotification(userId, "Đơn hàng bị hủy một phần");
//    } else {
//        // Mặc định = IN_PROGRESS
//        order.setStatus(OrderStatus.IN_PROGRESS.getName());
//        // Gửi noti "Đơn hàng đang xử lý"
//        notificationService.sendOrderStatusNotification(userId, "Đang xử lý");
//    }
//
//    // 8. Lưu lại Order
//    productOrderRepository.save(order);
//}

    @Override
    @Transactional
    public void updateOrderDetailStatus(Integer orderDetailId, OrderDetailStatus newStatus) {
        // 1. Lấy OrderDetail
        OrderDetail detail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy OrderDetail với ID: " + orderDetailId));

        // 2. Không cho đổi nếu đã DA_TIEM
        if (detail.getStatus() == OrderDetailStatus.DA_TIEM) {
            throw new IllegalStateException("OrderDetail đã ở trạng thái Đã tiêm, không thể thay đổi trạng thái!");
        }

        // 3. Gán trạng thái mới
        detail.setStatus(newStatus);
        orderDetailRepository.save(detail);

        // 4. Lấy ProductOrder (để cập nhật OrderStatus & lấy user)
        String orderId = detail.getOrderId();
        ProductOrder order = productOrderRepository.findByOrderId(orderId);
        if (order == null) {
            throw new NoSuchElementException("Không tìm thấy ProductOrder với orderId: " + orderId);
        }

        // 5. Lấy userId từ đơn hàng (hoặc từ detail, tùy cấu trúc)
        Long userId = order.getUser().getId();

        // 6. Xử lý logic + thông báo theo từng trạng thái
        switch (newStatus) {
            case DA_LEN_LICH:
                // Gửi email
                emailService.sendVaccinationUpdateEmail(detail);
                // Tạo message notify
                notificationService.sendNotification(userId,
                        "Chi tiết đơn hàng " + orderId + " đã lên lịch tiêm chủng. Vui lòng kiểm tra thông tin.");
                break;

            case DA_TIEM:
                // Trừ tồn kho - Chỉ giảm số lượng trong 1 lô (ProductDetail)
                Product product = detail.getProduct();
                ProductDetails productDetail = detail.getProductDetails(); // Lấy ProductDetail (lô hàng) liên quan đến OrderDetail

                if (productDetail != null) {
                    int qty = detail.getQuantity(); // Số lượng đã đặt
                    if (productDetail.getQuantity() - productDetail.getReservedQuantity() >= qty) {
                        // Giảm số lượng trong ProductDetail và cập nhật lại reserved_quantity về 0
                        productDetail.setReservedQuantity(productDetail.getReservedQuantity() - qty); // Giảm reserved_quantity
                        product.setReservedQuantity(product.getReservedQuantity() - qty); // Cập nhật tổng reservedQuantity trong Product
                        productRepository.save(product); // Lưu lại Product sau khi cập nhật
                        productDetail.setQuantity(productDetail.getQuantity() - qty); // Giảm quantity trong ProductDetail nếu cần
                    }
                }

                // Gửi email
                emailService.sendCustomEmail(
                        detail.getEmail(),
                        "Xác nhận đã tiêm vaccine",
                        "Chi tiết đơn hàng " + orderId + " đã được tiêm thành công."
                );
                // Gửi thông báo trong hệ thống
                notificationService.sendNotification(userId,
                        "Chi tiết đơn hàng " + orderId + " đã được tiêm thành công.");
                break;

            case DA_HUY:
                // Gửi email
                emailService.sendCustomEmail(
                        detail.getEmail(),
                        "Xác nhận hủy chi tiết tiêm",
                        "Chi tiết đơn hàng " + orderId + " đã bị hủy."
                );
                // Gửi thông báo trong hệ thống
                notificationService.sendNotification(userId,
                        "Chi tiết đơn hàng " + orderId + " đã bị hủy.");
                break;

            // Nếu còn trạng thái khác thì ta xử lý tương tự
            default:
                // CHUA_TIEM, QUA_HAN,... tuỳ bạn
                break;
        }

        // 7. Tổng hợp các OrderDetail để suy ra OrderStatus
        List<OrderDetail> allDetails = orderDetailRepository.findByOrderId(orderId);

        boolean allCanceled = allDetails.stream().allMatch(d -> d.getStatus() == OrderDetailStatus.DA_HUY);
        boolean allDone = allDetails.stream().allMatch(d -> d.getStatus() == OrderDetailStatus.DA_TIEM);
        boolean anyCanceled = allDetails.stream().anyMatch(d -> d.getStatus() == OrderDetailStatus.DA_HUY);

        /*
         *  Nếu toàn bộ chi tiết DA_HUY --> OrderStatus = CANCEL
         *  Nếu toàn bộ chi tiết DA_TIEM --> OrderStatus = SUCCESS
         *  Nếu có ít nhất một cái DA_HUY, còn lại chưa xong --> CANCELED_PARTIAL
         *  Ngược lại (chưa tiêm xong, chưa hủy hết) --> IN_PROGRESS
         */
        if (allCanceled) {
            order.setStatus(OrderStatus.CANCEL.getName());
            // Ví dụ: gửi noti "Đơn hàng đã hủy toàn bộ"
            notificationService.sendOrderStatusNotification(userId, "Đã hủy toàn bộ");
        } else if (allDone) {
            order.setStatus(OrderStatus.SUCCESS.getName());
            // Gửi noti "Đơn hàng đã tiêm xong"
            notificationService.sendOrderStatusNotification(userId, "Đã tiêm xong");
        } else if (anyCanceled) {
            order.setStatus(OrderStatus.CANCELED_PARTIAL.getName());
            // Gửi noti "Đơn hàng bị hủy một phần"
            notificationService.sendOrderStatusNotification(userId, "Đơn hàng bị hủy một phần");
        } else {
            // Mặc định = IN_PROGRESS
            order.setStatus(OrderStatus.IN_PROGRESS.getName());
            // Gửi noti "Đơn hàng đang xử lý"
            notificationService.sendOrderStatusNotification(userId, "Đang xử lý");
        }

        // 8. Lưu lại Order
        productOrderRepository.save(order);
    }



    //STAFF cập nhật lịch tiêm
    @Override
    @Transactional
    public OrderDetail updateVaccinationDate(Long orderDetailId, LocalDateTime vaccinationDate) {
        OrderDetail orderDetail = orderDetailRepository.findById(Math.toIntExact(orderDetailId))
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy OrderDetail với ID: " + orderDetailId));

        orderDetail.setVaccinationDate(vaccinationDate);
        orderDetail.setStatus(OrderDetailStatus.DA_LEN_LICH);
        orderDetailRepository.save(orderDetail);

        // Gửi email thông báo cho khách hàng
        emailService.sendVaccinationUpdateEmail(orderDetail);

        // Gửi notification cho khách hàng
        notificationService.sendNotification(
                orderDetail.getChild().getParentid(),
                "Lịch tiêm chủng của bạn cho vaccine " + orderDetail.getProduct().getTitle()
                        + " đã được cập nhật vào ngày " + vaccinationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return orderDetail;
    }

    @Override
    public List<OrderDetailResponse> getUpcomingSchedules(LocalDate date, OrderDetailStatus status) {
        List<OrderDetail> all = orderDetailRepository.findAllForUpcoming();

        // Lọc trong Java
        Stream<OrderDetail> stream = all.stream();

        if (date != null) {
            stream = stream.filter(od -> od.getVaccinationDate() != null
                    && od.getVaccinationDate().toLocalDate().isEqual(date));
        }

        if (status != null) {
            stream = stream.filter(od -> od.getStatus().equals(status));
        }

        List<OrderDetail> filtered = stream.toList();

        return filtered.stream().map(od -> new OrderDetailResponse(
                od.getId(),
                od.getProduct().getTitle(),
                od.getQuantity(),
                od.getOrderId(),
                od.getVaccinationDate(),
                od.getProduct().getDiscountPrice(),
                od.getFirstName(),
                od.getLastName(),
                od.getEmail(),
                od.getMobileNo(),
                od.getStatus().name(),
                od.getChild().getFullname(),
                od.getChild().getId()
        )).collect(Collectors.toList());
    }


    @Override
    public List<OrderDetailResponse> getWeeklySchedule(LocalDate startDate) {
        if (startDate == null) {
            // Mặc định lấy ngày thứ 2 của tuần hiện tại
            LocalDate now = LocalDate.now();
            startDate = now.with(DayOfWeek.MONDAY);
        }

        LocalDateTime start = startDate.atTime(0, 0);
        LocalDateTime end = startDate.plusDays(6).atTime(23, 59);

        List<OrderDetail> list = orderDetailRepository.findSchedulesByWeek(start, end);

        return list.stream().map(od -> new OrderDetailResponse(
                od.getId(),
                od.getProduct().getTitle(),
                od.getQuantity(),
                od.getOrderId(),
                od.getVaccinationDate(),
                od.getProduct().getDiscountPrice(),
                od.getFirstName(),
                od.getLastName(),
                od.getEmail(),
                od.getMobileNo(),
                od.getStatus().name(),
                od.getChild().getFullname(),
                od.getChild().getId()
        )).collect(Collectors.toList());
    }

    @Override
    public List<OrderDetailResponse> getUpcomingSchedulesWithoutStatus(LocalDate date) {
        List<OrderDetail> all = orderDetailRepository.findAllForUpcoming();

        // Lọc theo ngày nếu có
        Stream<OrderDetail> stream = all.stream();

        if (date != null) {
            stream = stream.filter(od ->
                    od.getVaccinationDate() != null &&
                            od.getVaccinationDate().toLocalDate().isEqual(date));
        }

        return stream.map(od -> new OrderDetailResponse(
                od.getId(),
                od.getProduct().getTitle(),
                od.getQuantity(),
                od.getOrderId(),
                od.getVaccinationDate(),
                od.getProduct().getDiscountPrice(),
                od.getFirstName(),
                od.getLastName(),
                od.getEmail(),
                od.getMobileNo(),
                od.getStatus().name(),
                od.getChild().getFullname(),
                od.getChild().getId(),
                od.getStaffid() != null ? od.getStaffid().getId() : null,
                od.getStaffName()
        )).collect(Collectors.toList());
    }

    //Method lấy bệnh nền trẻ
    public Set<String> getConditionNames(User child){
        return child.getUnderlyingConditions()
                .stream()
                .map(UnderlyingCondition::getConditionName)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }



//    @Override
//    public List<ProductSuggestionResponse> suggestVaccinesForChild(Long childId) {
//    Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    Long userId = jwt.getClaim("id");
//
//    // Lấy thông tin của trẻ từ cơ sở dữ liệu
//    User child = userRepository.findByIdDirect(childId);
//    if (child == null || !Objects.equals(child.getParentid(), userId)) {
//        throw new IllegalArgumentException("Đây không phải là trẻ của bạn");
//    }
//
//    // Tính độ tuổi của trẻ theo tháng từ ngày sinh (bod) và ngày hiện tại
//    int ageInMonths = Period.between(child.getBod(), LocalDate.now()).getYears() * 12
//            + Period.between(child.getBod(), LocalDate.now()).getMonths();
//
//    // Tạo danh sách để chứa các kết quả gợi ý vaccine cho các nhóm tuổi
//    List<ProductSuggestionResponse> suggestedVaccines = new ArrayList<>();
//
//    // Chia phạm vi độ tuổi thành các nhóm
//    List<AgeGroup> ageGroups = new ArrayList<>();
//
//    //Check bệnh nền
//    Set<String> childConds = getConditionNames(child);
//
//    // Kiểm tra và thêm các nhóm vào danh sách
//    if (ageInMonths <= 3) {
//        ageGroups.add(AgeGroup.AGE_0_3);
//    }
//    if (ageInMonths >= 4 && ageInMonths <= 6) {
//        ageGroups.add(AgeGroup.AGE_4_6);
//    }
//    if (ageInMonths >= 7 && ageInMonths <= 12) {
//        ageGroups.add(AgeGroup.AGE_7_12);
//    }
//    if (ageInMonths >= 13 && ageInMonths <= 24) {
//        ageGroups.add(AgeGroup.AGE_13_24);
//    }
//    if (ageInMonths >= 25) {
//        ageGroups.add(AgeGroup.AGE_25_PLUS);
//    }
//
//    // Lọc vaccine cho mỗi nhóm tuổi
//    List<Product> allProducts = productRepository.findAll();
//    for (AgeGroup group : ageGroups) {
//        final AgeGroup ageGroup = group;
//
//        List<ProductSuggestionResponse> groupVaccines = allProducts.stream()
//                .filter(product -> product.getTargetGroup().stream()
//                        .anyMatch(productAgeGroup -> productAgeGroup.getAgeGroup() == ageGroup))  // Kiểm tra xem sản phẩm có thuộc nhóm tuổi này không
//
//                // lọc thêm theo bệnh nền
//                .filter(p -> {
//                    List<String> conds = p.getUnderlyingConditions();
//                    // Vaccine dùng chung (không liệt kê bệnh nền) => gợi ý
//                    if (conds.isEmpty()) return true;
//                    // Nếu có giao cắt => KHÔNG gợi ý
//                    return conds.stream().noneMatch(c -> childConds.contains(c.toLowerCase()));
//                })
//
//                .map(product -> new ProductSuggestionResponse(
//                        product.getId(),
//                        product.getTitle(),
//                        product.getDescription(),
//                        product.getImage(),
//                        product.getPrice(),
//                        product.getDiscountPrice(),
//                        product.getIsPriority(),
//                        product.getManufacturer(),
//                        product.getMinAgeMonths(),
//                        product.getMaxAgeMonths(),
//                        product.getNumberOfDoses(),
//                        product.getQuantity()))
//                .collect(Collectors.toList());
//
//        // Thêm vaccine của nhóm hiện tại vào danh sách chung
//        suggestedVaccines.addAll(groupVaccines);
//    }
//
//    return suggestedVaccines;
//}

    @Override
    public List<ProductSuggestionResponse> suggestVaccinesForChild(Long childId) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");

        // Lấy thông tin của trẻ từ cơ sở dữ liệu
        User child = userRepository.findByIdDirect(childId);
        if (child == null || !Objects.equals(child.getParentid(), userId)) {
            throw new IllegalArgumentException("Đây không phải là trẻ của bạn");
        }

        // Tính độ tuổi của trẻ theo tháng từ ngày sinh (bod) và ngày hiện tại
        int ageInMonths = Period.between(child.getBod(), LocalDate.now()).getYears() * 12
                + Period.between(child.getBod(), LocalDate.now()).getMonths();

        // Tạo danh sách để chứa các kết quả gợi ý vaccine cho các nhóm tuổi
        List<ProductSuggestionResponse> suggestedVaccines = new ArrayList<>();

        // Chia phạm vi độ tuổi thành các nhóm
        List<AgeGroup> ageGroups = new ArrayList<>();

        //Check bệnh nền
        Set<String> childConds = getConditionNames(child);

        // Kiểm tra và thêm các nhóm vào danh sách
        if (ageInMonths <= 3) {
            ageGroups.add(AgeGroup.AGE_0_3);
        }
        if (ageInMonths >= 4 && ageInMonths <= 6) {
            ageGroups.add(AgeGroup.AGE_4_6);
        }
        if (ageInMonths >= 7 && ageInMonths <= 12) {
            ageGroups.add(AgeGroup.AGE_7_12);
        }
        if (ageInMonths >= 13 && ageInMonths <= 24) {
            ageGroups.add(AgeGroup.AGE_13_24);
        }
        if (ageInMonths >= 25) {
            ageGroups.add(AgeGroup.AGE_25_PLUS);
        }

        // Lọc vaccine cho mỗi nhóm tuổi
        List<Product> allProducts = productRepository.findAll();
        for (AgeGroup group : ageGroups) {
            final AgeGroup ageGroup = group;

            // Kiểm tra nhóm AGE_ALL
            if (ageGroup == AgeGroup.AGE_ALL) {
                // Nếu là nhóm AGE_ALL, gợi ý tất cả vaccine không phân biệt độ tuổi
                List<ProductSuggestionResponse> groupVaccines = allProducts.stream()
                        .filter(product -> product.getTargetGroup().stream()
                                .anyMatch(productAgeGroup -> productAgeGroup.getAgeGroup() == AgeGroup.AGE_ALL)) // Kiểm tra nếu vaccine này có AGE_ALL
                        .map(product -> new ProductSuggestionResponse(
                                product.getId(),
                                product.getTitle(),
                                product.getDescription(),
                                product.getImage(),
                                product.getPrice(),
                                product.getDiscountPrice(),
                                product.getIsPriority(),
                                product.getManufacturer(),
                                product.getMinAgeMonths(),
                                product.getMaxAgeMonths(),
                                product.getNumberOfDoses(),
                                product.getQuantity()))
                        .collect(Collectors.toList());

                // Thêm vaccine của nhóm AGE_ALL vào danh sách chung
                suggestedVaccines.addAll(groupVaccines);
            } else {
                // Các nhóm tuổi còn lại
                List<ProductSuggestionResponse> groupVaccines = allProducts.stream()
                        .filter(product -> product.getTargetGroup().stream()
                                .anyMatch(productAgeGroup -> productAgeGroup.getAgeGroup() == ageGroup))  // Kiểm tra sản phẩm phù hợp nhóm tuổi
                        .filter(p -> {
                            List<String> conds = p.getUnderlyingConditions();
                            if (conds.isEmpty()) return true;
                            return conds.stream().noneMatch(c -> childConds.contains(c.toLowerCase()));
                        })
                        .map(product -> new ProductSuggestionResponse(
                                product.getId(),
                                product.getTitle(),
                                product.getDescription(),
                                product.getImage(),
                                product.getPrice(),
                                product.getDiscountPrice(),
                                product.getIsPriority(),
                                product.getManufacturer(),
                                product.getMinAgeMonths(),
                                product.getMaxAgeMonths(),
                                product.getNumberOfDoses(),
                                product.getQuantity()))
                        .collect(Collectors.toList());

                // Thêm vaccine của nhóm hiện tại vào danh sách chung
                suggestedVaccines.addAll(groupVaccines);
            }
        }

        return suggestedVaccines;
    }




//    @Override
//    public List<ProductSuggestionResponse> suggestVaccinesByStaff(Long childId) {
//        // Lấy thông tin trẻ từ cơ sở dữ liệu (hoặc trả về lỗi nếu không tìm thấy trẻ)
//        Optional<User> optionalChild = Optional.ofNullable(userRepository.findByIdDirect(childId));
//        if (!optionalChild.isPresent()) {
//            throw new IllegalArgumentException("Trẻ không tồn tại trong hệ thống.");
//        }
//
//        User child = optionalChild.get();
//
//        // Tính toán độ tuổi của trẻ theo tháng từ ngày sinh (bod) và ngày hiện tại
//        int ageInMonths = Period.between(child.getBod(), LocalDate.now()).getYears() * 12
//                + Period.between(child.getBod(), LocalDate.now()).getMonths();
//
//        //Lọc bệnh nền
//        Set<String> childConds = getConditionNames(child);
//
//
//        // Lọc các sản phẩm vaccine phù hợp với độ tuổi của trẻ
//        List<Product> suitable = productRepository.findSuitableProductsForAge(ageInMonths);
//
//
//        // Lọc các sản phẩm vaccine có số mũi còn lại > 0
//        return suitable.stream()
//                .filter(product -> product.getTargetGroup().stream()
//                        .anyMatch(productAgeGroup ->
//                                productAgeGroup.getAgeGroup().getMinMonth() <= ageInMonths &&
//                                        productAgeGroup.getAgeGroup().getMaxMonth() >= ageInMonths)) // Kiểm tra nhóm tuổi của sản phẩm
//
//                .filter(p -> {
//                    List<String> conds = p.getUnderlyingConditions();
//                    return conds.isEmpty() ||
//                            conds.stream().noneMatch(c -> childConds.contains(c.toLowerCase()));
//                })
//                .filter(product -> {
//                    int taken = orderDetailRepository.countDosesTaken(product.getId(), childId);
//                    int remaining = product.getNumberOfDoses() - taken;
//                    return remaining > 0; // Chỉ chọn những vaccine còn mũi tiêm
//                })
//                .map(product -> new ProductSuggestionResponse(
//                        product.getId(),
//                        product.getTitle(),
//                        product.getDescription(),
//                        product.getImage(),
//                        product.getPrice(),
//                        product.getDiscountPrice(),
//                        product.getIsPriority(),
//                        product.getManufacturer(),
//                        product.getMinAgeMonths(),
//                        product.getMaxAgeMonths(),
//                        product.getNumberOfDoses(),
//                        product.getQuantity()
//                ))
//                .collect(Collectors.toList()); // Trả về danh sách các vaccine gợi ý
//    }
@Override
public List<ProductSuggestionResponse> suggestVaccinesByStaff(Long childId) {
    // Lấy thông tin trẻ từ cơ sở dữ liệu (hoặc trả về lỗi nếu không tìm thấy trẻ)
    Optional<User> optionalChild = Optional.ofNullable(userRepository.findByIdDirect(childId));
    if (!optionalChild.isPresent()) {
        throw new IllegalArgumentException("Trẻ không tồn tại trong hệ thống.");
    }

    User child = optionalChild.get();

    // Tính toán độ tuổi của trẻ theo tháng từ ngày sinh (bod) và ngày hiện tại
    int ageInMonths = Period.between(child.getBod(), LocalDate.now()).getYears() * 12
            + Period.between(child.getBod(), LocalDate.now()).getMonths();

    // Lọc bệnh nền
    Set<String> childConds = getConditionNames(child);

    // Lọc các sản phẩm vaccine phù hợp với độ tuổi của trẻ
    List<Product> suitable = productRepository.findSuitableProductsForAge(ageInMonths);

    // Lọc các sản phẩm vaccine có số mũi còn lại > 0
    return suitable.stream()
            .filter(product -> product.getTargetGroup().stream()
                    .anyMatch(productAgeGroup ->
                            productAgeGroup.getAgeGroup().getMinMonth() <= ageInMonths &&
                                    productAgeGroup.getAgeGroup().getMaxMonth() >= ageInMonths ||
                                    productAgeGroup.getAgeGroup() == AgeGroup.AGE_ALL)) // Kiểm tra AGE_ALL
            .filter(p -> {
                List<String> conds = p.getUnderlyingConditions();
                return conds.isEmpty() ||
                        conds.stream().noneMatch(c -> childConds.contains(c.toLowerCase()));
            })
            .filter(product -> {
                int taken = orderDetailRepository.countDosesTaken(product.getId(), childId);
                int remaining = product.getNumberOfDoses() - taken;
                return remaining > 0; // Chỉ chọn những vaccine còn mũi tiêm
            })
            .map(product -> new ProductSuggestionResponse(
                    product.getId(),
                    product.getTitle(),
                    product.getDescription(),
                    product.getImage(),
                    product.getPrice(),
                    product.getDiscountPrice(),
                    product.getIsPriority(),
                    product.getManufacturer(),
                    product.getMinAgeMonths(),
                    product.getMaxAgeMonths(),
                    product.getNumberOfDoses(),
                    product.getQuantity()
            ))
            .collect(Collectors.toList());
}





    @Override
    @Transactional
    public void cancelOrderByCustomer(String orderId, Long userId, String reason) throws AccessDeniedException {
        ProductOrder order = productOrderRepository.findByOrderId(orderId);
        if (order == null) {
            throw new NoSuchElementException("Không tìm thấy đơn hàng");
        }

        if (!order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Bạn không có quyền hủy đơn hàng này");
        }

        // Check đã tiêm chưa
        List<OrderDetail> details = orderDetailRepository.findByOrderId(orderId);
        boolean hasInjected = details.stream()
                .anyMatch(d -> {
                    OrderDetailStatus status = d.getStatus();
                    return OrderDetailStatus.DA_TIEM.equals(status) || OrderDetailStatus.DA_LEN_LICH.equals(status);
                });

        // Lưu lý do hủy nếu có
        if (reason != null && !reason.trim().isEmpty()) {
            order.setCancellationReason(reason);
        }


        if (hasInjected) {
            throw new IllegalStateException("Không thể hủy đơn vì đã có mũi đã tiêm hoặc liên hệ thông qua số hotline để được hướng dẫn.");
        }

        // Cập nhật trạng thái đơn hàng
        order.setStatus(CANCEL.getName());
        productOrderRepository.save(order);

        for (OrderDetail detail : details) {
            detail.setStatus(OrderDetailStatus.DA_HUY);
           // detail.setVaccinationDate(null);

            // Trả lại reservedQuantity
            Product product = detail.getProduct();
            product.setReservedQuantity(product.getReservedQuantity() - 1);
            productRepository.save(product);
        }

        orderDetailRepository.saveAll(details);
    }


    @Override
    @Transactional
    public void cancelOrderByStaff(String orderId, String reason) {
        // 1. Lấy ra ProductOrder
        ProductOrder order = productOrderRepository.findByOrderId(orderId);
        if (order == null) {
            throw new NoSuchElementException("Không tìm thấy đơn hàng");
        }

        // 2. Lấy danh sách OrderDetail
        List<OrderDetail> details = orderDetailRepository.findByOrderId(orderId);

        // 2.1. Nếu Order đã SUCCESS => không cho hủy
        if (OrderStatus.SUCCESS.getName().equals(order.getStatus())) {
            throw new IllegalStateException("Đơn hàng đã ở trạng thái SUCCESS, không thể hủy!");
        }

        // 2.2. Kiểm tra xem tất cả chi tiết đều DA_TIEM hay không
        boolean allInjected = details.stream()
                .allMatch(d -> OrderDetailStatus.DA_TIEM.equals(d.getStatus()));

        // Nếu toàn bộ chi tiết DA_TIEM => không cho hủy
        if (allInjected) {
            throw new IllegalStateException("Tất cả các mũi trong đơn hàng này đều đã tiêm, không thể hủy!");
        }

        // 3. Xác định xem có chi tiết nào đã DA_TIEM không
        boolean hasInjected = details.stream()
                .anyMatch(d -> OrderDetailStatus.DA_TIEM.equals(d.getStatus()));

        // 4. Thực hiện logic hủy
        if (hasInjected) {
            // Một số chi tiết đã tiêm => chỉ hủy phần còn lại
            for (OrderDetail detail : details) {
                if (!OrderDetailStatus.DA_TIEM.equals(detail.getStatus())) {
                    detail.setStatus(OrderDetailStatus.DA_HUY);
                    // detail.setVaccinationDate(null);

                    Product p = detail.getProduct();
                    p.setReservedQuantity(p.getReservedQuantity() - detail.getQuantity());
                    productRepository.save(p);
                }
            }
            // Đơn hàng -> CANCELED_PARTIAL
            order.setStatus(OrderStatus.CANCELED_PARTIAL.getName());

        } else {
            // Không có chi tiết nào DA_TIEM => Hủy toàn bộ
            for (OrderDetail detail : details) {
                detail.setStatus(OrderDetailStatus.DA_HUY);
                //detail.setVaccinationDate(null);

                Product p = detail.getProduct();
                p.setReservedQuantity(p.getReservedQuantity() - detail.getQuantity());
                productRepository.save(p);
            }
            // Đơn hàng -> CANCEL
            order.setStatus(OrderStatus.CANCEL.getName());
        }

        // 5. Lưu lại lý do hủy
        if (reason != null && !reason.trim().isEmpty()) {
            order.setCancellationReason(reason);
        }

        // 6. Lưu thay đổi
        productOrderRepository.save(order);
        orderDetailRepository.saveAll(details);

        // 7. Gửi mail xác nhận hủy
        emailService.sendCancelOrderEmailWithReason(order, details);
    }


    @Override
    public List<OrderDetailResponse> getUpcomingSchedulesForStaff(LocalDateTime fromDate, OrderDetailStatus status) {
        List<OrderDetail> details = orderDetailRepository.findUpcomingSchedules(fromDate, status);

        return details.stream().map(od -> new OrderDetailResponse(
                od.getId(),
                od.getProduct().getTitle(),
                od.getQuantity(),
                od.getOrderId(),
                od.getVaccinationDate(),
                od.getProduct().getDiscountPrice(),
                od.getFirstName(),
                od.getLastName(),
                od.getEmail(),
                od.getMobileNo(),
                od.getStatus().name(),
                od.getStaffid().getId(),
                od.getStaffName()
        )).toList();
    }


    @Override
    public List<OrderDetailResponse> getUpcomingSchedulesForParent(Long parentId, LocalDateTime fromDate, OrderDetailStatus status) {
        List<OrderDetail> details = orderDetailRepository.findUpcomingSchedulesForParent(parentId, fromDate, status);

        return details.stream().map(od -> new OrderDetailResponse(
                od.getId(),
                od.getProduct().getTitle(),
                od.getQuantity(),
                od.getOrderId(),
                od.getVaccinationDate(),
                od.getProduct().getPrice(),
                od.getFirstName(),
                od.getLastName(),
                od.getEmail(),
                od.getMobileNo(),
                od.getStatus().name(),
                od.getChild().getFullname(),
                od.getChild().getId()
        )).toList();
    }

    //=======================================3
//    @Override
//    @Transactional
//    public ProductOrder createOrderByChildProductMap(Map<Long, List<Long>> childProductMap, OrderRequest orderRequest) {
//        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long userId = jwt.getClaim("id");
//
//        // Lấy thông tin phụ huynh từ userId
//        User parent = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
//
//        List<String> errors = new ArrayList<>();  // Danh sách lưu trữ lỗi khi tạo đơn
//        List<OrderDetail> orderDetails = new ArrayList<>();  // Danh sách lưu trữ chi tiết đơn hàng
//        double totalPrice = 0.0;  // Biến lưu trữ tổng giá trị đơn hàng
//
//        // Lấy ngày tiêm đầu tiên từ yêu cầu
//        LocalDateTime firstVaccinationDate = orderRequest.getVaccinationdate();
//        if (firstVaccinationDate != null) {
//            // Kiểm tra xem ngày tiêm có hợp lệ không
//            if (firstVaccinationDate.isBefore(LocalDateTime.now())) {
//                throw new IllegalArgumentException("Ngày tiêm mong muốn phải từ hôm nay trở đi.");
//            }
//            LocalTime time = firstVaccinationDate.toLocalTime();
//            if (time.isBefore(LocalTime.of(7, 30)) || time.isAfter(LocalTime.of(17, 0))) {
//                // Kiểm tra giờ tiêm có nằm trong khung giờ làm việc không
//                throw new IllegalArgumentException("Giờ tiêm chỉ được chọn từ 07:30 đến 17:00.");
//            }
//        }
//
//        // Lấy thông tin bác sĩ tiêm (STAFF) tự động
//        User staffUser = getStaffUser();
//
//        // Tạo đơn hàng
//        ProductOrder order = new ProductOrder();
//        order.setOrderId("ORD" + System.currentTimeMillis());  // ID đơn hàng
//        order.setOrderDate(LocalDate.now());  // Ngày đặt hàng
//        order.setStatus(OrderStatus.ORDER_RECEIVED.getName());  // Trạng thái đơn hàng
//        order.setPaymentType("VNPAY");  // Loại thanh toán
//        order.setUser(parent);  // Gán phụ huynh làm người tạo đơn hàng
//        productOrderRepository.save(order);  // Lưu đơn hàng vào cơ sở dữ liệu
//
//        // Duyệt qua từng trẻ và danh sách sản phẩm
//        for (Map.Entry<Long, List<Long>> entry : childProductMap.entrySet()) {
//            Long childId = entry.getKey();  // ID trẻ
//            List<Long> productIds = entry.getValue().stream().distinct().toList();  // Danh sách sản phẩm
//
//            // Lấy thông tin trẻ từ ID
//            User child = userRepository.findByIdDirect(childId);
//            if (child == null || !Objects.equals(child.getParentid(), userId)) {
//                // Nếu không tìm thấy trẻ hoặc trẻ không thuộc phụ huynh, thêm lỗi vào danh sách
//                errors.add("Trẻ ID " + childId + " không hợp lệ hoặc không thuộc phụ huynh.");
//                continue;
//            }
//
//            // Duyệt qua từng sản phẩm
//            for (Long productId : productIds) {
//                Product product = productRepository.findById(productId).orElse(null);
//                if (product == null) {
//                    // Nếu không tìm thấy sản phẩm, thêm lỗi vào danh sách
//                    errors.add("Không tìm thấy sản phẩm với ID: " + productId);
//                    continue;
//                }
//
//                // Kiểm tra số lượng sản phẩm có sẵn từ tất cả các ProductDetails
//                int available = product.getQuantity() - product.getReservedQuantity();
//                if (available < 1) {
//                    // Nếu số lượng sản phẩm không đủ, thêm lỗi vào danh sách
//                    errors.add("Sản phẩm \"" + product.getTitle() + "\" không còn đủ số lượng cho bé " + child.getFullname());
//                    continue;
//                }
//
//                // Duyệt qua các ProductDetails của sản phẩm để giảm số lượng từ các lô có đủ
//                int quantityRequested = 1;  // Giả sử yêu cầu là 1 sản phẩm, có thể điều chỉnh theo yêu cầu thực tế
//                ProductDetails selectedProductDetail = null;
//                for (ProductDetails productDetail : product.getProductDetails()) {
//                    if (productDetail.getQuantity() - productDetail.getReservedQuantity() > 0) {
//                        // Kiểm tra ngày hết hạn của lô (ProductDetail) có hợp lệ (sau ngày tiêm)
//                        if (productDetail.getExpirationDate().isAfter(firstVaccinationDate.toLocalDate())) {
//                            selectedProductDetail = productDetail;
//                            int quantityToReserve = Math.min(quantityRequested, productDetail.getQuantity() - productDetail.getReservedQuantity());
//                            productDetail.updateReservedQuantity(quantityToReserve);
//                            quantityRequested -= quantityToReserve;
//
//                            // Cập nhật số lượng đã đặt cho sản phẩm
//                            product.setReservedQuantity(product.getReservedQuantity() + quantityToReserve);
//
//                            // Nếu đã đủ số lượng, thoát khỏi vòng lặp
//                            if (quantityRequested == 0) {
//                                break;
//                            }
//                        }
//                    }
//                }
//
//                // Kiểm tra xem còn đủ số lượng để đặt không
//                if (quantityRequested > 0) {
//                    errors.add("Không đủ số lượng vaccine \"" + product.getTitle() + "\" cho bé " + child.getFullname());
//                    continue;
//                }
//
//                // Tính tuổi của trẻ tại ngày tiêm hoặc hôm nay
//                LocalDate dob = child.getBod();  // Ngày sinh của trẻ
//                LocalDate referenceDate = firstVaccinationDate != null ? firstVaccinationDate.toLocalDate() : LocalDate.now();  // Ngày tham chiếu
//                Period age = Period.between(dob, referenceDate);  // Tính độ tuổi
//                int childAgeInMonths = age.getYears() * 12 + age.getMonths();  // Chuyển đổi tuổi thành tháng
//
//                // Kiểm tra độ tuổi có phù hợp với sản phẩm không
//                if (childAgeInMonths < product.getMinAgeMonths() || childAgeInMonths > product.getMaxAgeMonths()) {
//                    errors.add("Bé " + child.getFullname() + " không phù hợp với độ tuổi tiêm của vaccine \"" + product.getTitle() + "\" (tuổi hiện tại: " + childAgeInMonths + " tháng).");
//                    continue;
//                }
//
//                // Kiểm tra xem vaccine đã được tiêm cho trẻ chưa
//                if (firstVaccinationDate != null) {
//                    boolean alreadyScheduledSameVaccine = orderDetailRepository
//                            .existsByChildIdAndProductIdAndVaccinationDate(childId, productId, firstVaccinationDate);
//
//                    if (alreadyScheduledSameVaccine) {
//                        errors.add("Bé " + child.getFullname()
//                                + " đã có lịch tiêm vaccine \"" + product.getTitle() + "\" vào "
//                                + firstVaccinationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
//                                + ". Vui lòng chọn thời gian khác hoặc bỏ chọn vaccine này.");
//                        continue;
//                    }
//
//                    boolean validVaccinationDate = isValidVaccinationDate(firstVaccinationDate, product, child);
//                    if (!validVaccinationDate) {
//                        errors.add("Ngày tiêm không hợp lệ đối với mũi vaccine \"" + product.getTitle() + "\". Vui lòng chọn lại ngày tiêm.");
//                        continue;
//                    }
//                }
//
//                // Tạo chi tiết đơn hàng cho sản phẩm
//                OrderDetail detail = new OrderDetail();
//                detail.setOrderId(order.getOrderId());
//                detail.setProduct(product);
//                detail.setChild(child);
//                detail.setQuantity(1);  // Số lượng sản phẩm
//                detail.setStatus(OrderDetailStatus.CHUA_TIEM);  // Trạng thái đơn hàng
//                detail.setFirstName(orderRequest.getFirstName());
//                detail.setLastName(orderRequest.getLastName());
//                detail.setEmail(orderRequest.getEmail());
//                detail.setMobileNo(orderRequest.getMobileNo());
//                detail.setVaccinationDate(firstVaccinationDate);  // Đặt ngày tiêm
//                detail.setStaffid(staffUser);  // Lưu bác sĩ tiêm vào order detail
//                detail.setStaffName(staffUser.getFullname());
//
//                orderDetails.add(detail);  // Thêm chi tiết vào danh sách
//                totalPrice += product.getPrice();  // Cộng dồn giá trị đơn hàng
//            }
//        }
//
//        // Nếu có lỗi, ném ra exception với thông báo lỗi
//        if (!errors.isEmpty()) {
//            throw new IllegalArgumentException("Không thể tạo đơn hàng vì lỗi:\n" + String.join("\n", errors));
//        }
//
//        // Lưu các chi tiết đơn hàng vào cơ sở dữ liệu
//        orderDetailRepository.saveAll(orderDetails);
//        // Cập nhật lại thông tin sản phẩm
//        productRepository.saveAll(orderDetails.stream().map(OrderDetail::getProduct).distinct().toList());
//        // Cập nhật tổng giá trị đơn hàng
//        order.setTotalPrice(totalPrice);
//        productOrderRepository.save(order);
//
//        return order;  // Trả về đơn hàng vừa tạo
//    }

    //================================================================================================2
    @Override
    @Transactional
    public ProductOrder createOrderByChildProductMap(Map<Long, List<Long>> childProductMap,
                                                     OrderRequest orderRequest) {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Long userId = jwt.getClaim("id");

        User parent = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        List<String> errors = new ArrayList<>();
        List<OrderDetail> orderDetails = new ArrayList<>();
        double totalPrice = 0.0;

        LocalDateTime firstVaccinationDate = orderRequest.getVaccinationdate();
        if (firstVaccinationDate != null) {
            if (firstVaccinationDate.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Ngày tiêm mong muốn phải từ hôm nay trở đi.");
            }
            LocalTime time = firstVaccinationDate.toLocalTime();
            if (time.isBefore(LocalTime.of(7, 30)) || time.isAfter(LocalTime.of(17, 0))) {
                throw new IllegalArgumentException("Giờ tiêm chỉ được chọn từ 07:30 đến 17:00.");
            }
        }

        User staffUser = getStaffUser();   // tự gán bác sĩ tiêm

        ProductOrder order = new ProductOrder();
        order.setOrderId("ORD" + System.currentTimeMillis());
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.ORDER_RECEIVED.getName());
        order.setPaymentType("VNPAY");
        order.setUser(parent);
        productOrderRepository.save(order);

        // ----- Duyệt map child‑product -----
        for (Map.Entry<Long, List<Long>> entry : childProductMap.entrySet()) {
            Long childId = entry.getKey();
            List<Long> productIds = entry.getValue().stream().distinct().toList();

            User child = userRepository.findByIdDirect(childId);
            if (child == null || !Objects.equals(child.getParentid(), userId)) {
                errors.add("Trẻ ID " + childId + " không hợp lệ hoặc không thuộc phụ huynh.");
                continue;
            }

            for (Long productId : productIds) {
                Product product = productRepository.findById(productId).orElse(null);
                if (product == null) {
                    errors.add("Không tìm thấy sản phẩm với ID: " + productId);
                    continue;
                }

                int available = product.getQuantity() - product.getReservedQuantity();
                if (available < 1) {
                    errors.add("Sản phẩm \"" + product.getTitle()
                            + "\" không còn đủ số lượng cho bé " + child.getFullname());
                    continue;
                }

                // ★ NEW: Chọn ProductDetails phù hợp (còn hàng & chưa hết hạn)
                ProductDetails selectedProductDetail = null;
                for (ProductDetails pd : product.getProductDetails()) {
                    boolean enough = pd.getQuantity() - pd.getReservedQuantity() >= 1;
                    boolean validExpiry = firstVaccinationDate == null
                            || pd.getExpirationDate().isAfter(firstVaccinationDate.toLocalDate());
                    if (enough && validExpiry) {
                        selectedProductDetail = pd;
                        break;
                    }
                }
                if (selectedProductDetail == null) {
                    errors.add("Không có lô (product_detail) hợp lệ cho vaccine \""
                            + product.getTitle() + "\".");
                    continue;
                }

                // ★ NEW: cập nhật reservedQuantity cho lô và sản phẩm
                selectedProductDetail.setReservedQuantity(
                        selectedProductDetail.getReservedQuantity() + 1);
                product.setReservedQuantity(product.getReservedQuantity() + 1);

                // ---------- Kiểm tra tuổi, lịch trùng... ----------
                LocalDate dob = child.getBod();
                LocalDate refDate = firstVaccinationDate != null
                        ? firstVaccinationDate.toLocalDate()
                        : LocalDate.now();
                int ageInMonths = Period.between(dob, refDate).getYears() * 12
                        + Period.between(dob, refDate).getMonths();

                if (ageInMonths < product.getMinAgeMonths()
                        || ageInMonths > product.getMaxAgeMonths()) {
                    errors.add("Bé " + child.getFullname()
                            + " không phù hợp độ tuổi tiêm vaccine \""
                            + product.getTitle() + "\".");
                    continue;
                }

                if (firstVaccinationDate != null) {
                    boolean duplicated = orderDetailRepository
                            .existsByChildIdAndProductIdAndVaccinationDate(
                                    childId, productId, firstVaccinationDate);
                    if (duplicated) {
                        errors.add("Bé " + child.getFullname()
                                + " đã có lịch tiêm vaccine \""
                                + product.getTitle() + "\" vào "
                                + firstVaccinationDate.format(
                                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + ".");
                        continue;
                    }
                    if (!isValidVaccinationDate(firstVaccinationDate, product, child)) {
                        errors.add("Ngày tiêm không hợp lệ cho vaccine \""
                                + product.getTitle() + "\".");
                        continue;
                    }
                }

                // ---------- Tạo OrderDetail ----------
                OrderDetail detail = new OrderDetail();
                detail.setOrderId(order.getOrderId());
                detail.setProduct(product);
                detail.setChild(child);
                detail.setQuantity(1);
                detail.setStatus(OrderDetailStatus.CHUA_TIEM);
                detail.setFirstName(orderRequest.getFirstName());
                detail.setLastName(orderRequest.getLastName());
                detail.setEmail(orderRequest.getEmail());
                detail.setMobileNo(orderRequest.getMobileNo());
                detail.setVaccinationDate(firstVaccinationDate);
                detail.setStaffid(staffUser);
                detail.setStaffName(staffUser.getFullname());

                // ★ NEW: lưu product_detail_id vào order detail
                detail.setProductDetails(selectedProductDetail);

                orderDetails.add(detail);
                totalPrice += product.getPrice();
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(
                    "Không thể tạo đơn hàng vì lỗi:\n" + String.join("\n", errors));
        }

        // ----- Lưu DB -----
        orderDetailRepository.saveAll(orderDetails);
        productRepository.saveAll(orderDetails.stream()
                .map(OrderDetail::getProduct)
                .distinct()
                .toList());
        order.setTotalPrice(totalPrice);
        productOrderRepository.save(order);

        return order;
    }




    private boolean isValidVaccinationDate(LocalDateTime vaccinationDate, Product product, User child) {
        // Lấy ngày tiêm gần nhất của trẻ đối với vaccine này
        Optional<LocalDateTime> lastVaccinationDateOpt = orderDetailRepository.findLastVaccinationDateByChildId(child.getId(), product.getId());

        // Nếu không có ngày tiêm gần nhất (trẻ chưa tiêm vaccine này), trả về true
        if (lastVaccinationDateOpt.isEmpty()) {
            return true; // Chấp nhận đặt lịch mới nếu chưa tiêm lần nào
        }

        // Lấy ngày tiêm gần nhất
        LocalDate lastVaccinationDate = lastVaccinationDateOpt.get().toLocalDate();

        // Tính số ngày giữa ngày tiêm gần nhất và ngày tiêm hiện tại
        long daysBetween = ChronoUnit.DAYS.between(lastVaccinationDate, vaccinationDate.toLocalDate());

        // Kiểm tra số ngày có lớn hơn hoặc bằng số ngày tối thiểu giữa các mũi tiêm không
        return daysBetween >= product.getMinDaysBetweenDoses();
    }


//    @Override
//    @Transactional
//    public ProductOrder createOrderByStaff(Long parentId, Map<Long, List<Long>> childProductMap, OrderRequest orderRequest) {
//        // Lấy thông tin staff đăng nhập
//        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long staffId = jwt.getClaim("id");
//
//        List<String> errors = new ArrayList<>();
//        List<OrderDetail> orderDetails = new ArrayList<>();
//        double totalPrice = 0.0;
//
//        // Kiểm tra parent
//        User parent = userRepository.findById(parentId)
//                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phụ huynh với ID: " + parentId));
//
//        // Kiểm tra ngày tiêm
//        LocalDateTime firstVaccinationDate = orderRequest.getVaccinationdate();
//        if (firstVaccinationDate != null) {
//            if (firstVaccinationDate.isBefore(LocalDateTime.now())) {
//                throw new IllegalArgumentException("Ngày tiêm mong muốn phải từ hôm nay trở đi.");
//            }
//            LocalTime time = firstVaccinationDate.toLocalTime();
//            if (time.isBefore(LocalTime.of(7, 30)) || time.isAfter(LocalTime.of(17, 0))) {
//                throw new IllegalArgumentException("Giờ tiêm chỉ được chọn từ 07:30 đến 17:00.");
//            }
//        }
//
//        // Tạo đơn hàng
//        ProductOrder order = new ProductOrder();
//        order.setOrderId("ORD" + System.currentTimeMillis());
//        order.setOrderDate(LocalDate.now());
//        order.setStatus(OrderStatus.ORDER_RECEIVED.getName());
//        order.setPaymentType("Trả tiền mặt tại hệ thống");
//        order.setUser(parent);
//        productOrderRepository.save(order);
//
//        // Lặp qua từng trẻ và vaccine
//        for (Map.Entry<Long, List<Long>> entry : childProductMap.entrySet()) {
//            Long childId = entry.getKey();
//            List<Long> productIds = entry.getValue().stream().distinct().toList();
//
//            User child = userRepository.findByIdDirect(childId);
//            if (child == null) {
//                errors.add("Không tìm thấy trẻ với ID: " + childId);
//                continue;
//            }
//
//            if (!Objects.equals(child.getParentid(), parentId)) {
//                errors.add("Trẻ ID " + childId + " không thuộc phụ huynh ID " + parentId);
//                continue;
//            }
//
//            // Kiểm tra từng vaccine cho trẻ
//            for (Long productId : productIds) {
//                Product product = productRepository.findById(productId).orElse(null);
//                if (product == null) {
//                    errors.add("Không tìm thấy sản phẩm ID: " + productId);
//                    continue;
//                }
//
//                int available = product.getQuantity() - product.getReservedQuantity();
//                if (available < 1) {
//                    errors.add("Sản phẩm \"" + product.getTitle() + "\" không còn đủ số lượng cho bé " + child.getFullname());
//                    continue;
//                }
//
//                // Kiểm tra độ tuổi của trẻ
//                LocalDate dob = child.getBod(); // Đảm bảo đã có trường này trong entity User
//                if (dob == null) {
//                    errors.add("Không tìm thấy ngày sinh của trẻ " + child.getFullname());
//                    continue;
//                }
//                LocalDate referenceDate = firstVaccinationDate != null ? firstVaccinationDate.toLocalDate() : LocalDate.now();
//                Period age = Period.between(dob, referenceDate);
//                int childAgeInMonths = age.getYears() * 12 + age.getMonths();
//
//                if (childAgeInMonths < product.getMinAgeMonths() || childAgeInMonths > product.getMaxAgeMonths()) {
//                    errors.add("Bé " + child.getFullname() + " không phù hợp với độ tuổi tiêm của vaccine \""
//                            + product.getTitle() + "\" (tuổi hiện tại: " + childAgeInMonths + " tháng).");
//                    continue;
//                }
//
//                // Kiểm tra ngày tiêm và khoảng cách giữa các mũi tiêm
//                if (firstVaccinationDate != null) {
//                    boolean alreadyScheduledSameVaccine = orderDetailRepository
//                            .existsByChildIdAndProductIdAndVaccinationDate(childId, productId, firstVaccinationDate);
//
//                    if (alreadyScheduledSameVaccine) {
//                        errors.add("Bé " + child.getFullname()
//                                + " đã có lịch tiêm vaccine \"" + product.getTitle() + "\" vào "
//                                + firstVaccinationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
//                                + ". Vui lòng chọn thời gian khác hoặc bỏ chọn vaccine này.");
//                        continue;
//                    }
//
//                    boolean isValidVaccinationDate = isValidVaccinationDate(firstVaccinationDate, product, child);
//                    if (!isValidVaccinationDate) {
//                        errors.add("Không thể đặt lịch vaccine \"" + product.getTitle() + "\" cho bé "
//                                + child.getFullname() + " vì chưa đủ thời gian giữa các mũi tiêm.");
//                        continue;
//                    }
//                }
//
//                // Tạo chi tiết đơn hàng
//                OrderDetail detail = new OrderDetail();
//                detail.setOrderId(order.getOrderId());
//                detail.setProduct(product);
//                detail.setChild(child);
//                detail.setQuantity(1);
//                detail.setStatus(OrderDetailStatus.CHUA_TIEM);
//                detail.setFirstName(orderRequest.getFirstName());
//                detail.setLastName(orderRequest.getLastName());
//                detail.setEmail(orderRequest.getEmail());
//                detail.setMobileNo(orderRequest.getMobileNo());
//
//                if (firstVaccinationDate != null) {
//                    detail.setVaccinationDate(firstVaccinationDate);
//                }
//
//                orderDetails.add(detail);
//                totalPrice += product.getPrice();
//                product.setReservedQuantity(product.getReservedQuantity() + 1);
//            }
//        }
//
//        // Nếu có lỗi, ném exception với tất cả lỗi gặp phải
//        if (!errors.isEmpty()) {
//            throw new IllegalArgumentException("Không thể tạo đơn hàng vì lỗi:\n" + String.join("\n", errors));
//        }
//
//        // Lưu các chi tiết đơn hàng và cập nhật lại số lượng đã đặt
//        orderDetailRepository.saveAll(orderDetails);
//        productRepository.saveAll(orderDetails.stream().map(OrderDetail::getProduct).distinct().toList());
//        order.setTotalPrice(totalPrice);
//        productOrderRepository.save(order);
//
//        return order;
//    }

    //=============================================================2
//    @Override
//    @Transactional
//    public ProductOrder createOrderByStaff(Long parentId, Map<Long, List<Long>> childProductMap, OrderRequest orderRequest) {
//        // Lấy thông tin staff đăng nhập
//        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long staffId = jwt.getClaim("id");
//
//        List<String> errors = new ArrayList<>();
//        List<OrderDetail> orderDetails = new ArrayList<>();
//        double totalPrice = 0.0;
//
//        // Kiểm tra parent
//        User parent = userRepository.findById(parentId)
//                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phụ huynh với ID: " + parentId));
//
//        // Kiểm tra ngày tiêm
//        LocalDateTime firstVaccinationDate = orderRequest.getVaccinationdate();
//        if (firstVaccinationDate != null) {
//            if (firstVaccinationDate.isBefore(LocalDateTime.now())) {
//                throw new IllegalArgumentException("Ngày tiêm mong muốn phải từ hôm nay trở đi.");
//            }
//            LocalTime time = firstVaccinationDate.toLocalTime();
//            if (time.isBefore(LocalTime.of(7, 30)) || time.isAfter(LocalTime.of(17, 0))) {
//                throw new IllegalArgumentException("Giờ tiêm chỉ được chọn từ 07:30 đến 17:00.");
//            }
//        }
//
//        // Tạo đơn hàng
//        ProductOrder order = new ProductOrder();
//        order.setOrderId("ORD" + System.currentTimeMillis());
//        order.setOrderDate(LocalDate.now());
//        order.setStatus(OrderStatus.ORDER_RECEIVED.getName());
//        order.setPaymentType("Trả tiền mặt tại hệ thống");
//        order.setUser(parent);
//        productOrderRepository.save(order);
//
//        // Lặp qua từng trẻ và vaccine
//        for (Map.Entry<Long, List<Long>> entry : childProductMap.entrySet()) {
//            Long childId = entry.getKey();
//            List<Long> productIds = entry.getValue().stream().distinct().toList();
//
//            User child = userRepository.findByIdDirect(childId);
//            if (child == null) {
//                errors.add("Không tìm thấy trẻ với ID: " + childId);
//                continue;
//            }
//
//            if (!Objects.equals(child.getParentid(), parentId)) {
//                errors.add("Trẻ ID " + childId + " không thuộc phụ huynh ID " + parentId);
//                continue;
//            }
//
//            // Kiểm tra từng vaccine cho trẻ
//            for (Long productId : productIds) {
//                Product product = productRepository.findById(productId).orElse(null);
//                if (product == null) {
//                    errors.add("Không tìm thấy sản phẩm ID: " + productId);
//                    continue;
//                }
//
//                int available = product.getQuantity() - product.getReservedQuantity();
//                if (available < 1) {
//                    errors.add("Sản phẩm \"" + product.getTitle() + "\" không còn đủ số lượng cho bé " + child.getFullname());
//                    continue;
//                }
//
//                // Duyệt qua các ProductDetails của sản phẩm để giảm số lượng từ các lô có đủ
//                int quantityRequested = 1;  // Giả sử yêu cầu là 1 sản phẩm, có thể điều chỉnh theo yêu cầu thực tế
//                for (ProductDetails productDetail : product.getProductDetails()) {
//                    // Kiểm tra ngày hết hạn của lô (ProductDetail) có hợp lệ (sau ngày tiêm)
//                    if (productDetail.getExpirationDate().isAfter(firstVaccinationDate.toLocalDate())) {
//                        if (productDetail.getQuantity() - productDetail.getReservedQuantity() > 0) {
//                            // Giảm số lượng từ ProductDetail
//                            int quantityToReserve = Math.min(quantityRequested, productDetail.getQuantity() - productDetail.getReservedQuantity());
//                            productDetail.updateReservedQuantity(quantityToReserve);
//                            quantityRequested -= quantityToReserve;
//
//                            // Cập nhật số lượng đã đặt cho sản phẩm
//                            product.setReservedQuantity(product.getReservedQuantity() + quantityToReserve);
//
//                            // Nếu đã đủ số lượng, thoát khỏi vòng lặp
//                            if (quantityRequested == 0) {
//                                break;
//                            }
//                        }
//                    }
//                }
//
//                // Kiểm tra xem còn đủ số lượng để đặt không
//                if (quantityRequested > 0) {
//                    errors.add("Không đủ số lượng vaccine \"" + product.getTitle() + "\" cho bé " + child.getFullname());
//                    continue;
//                }
//
//                // Tính tuổi của trẻ tại ngày tiêm hoặc hôm nay
//                LocalDate dob = child.getBod(); // Đảm bảo đã có trường này trong entity User
//                if (dob == null) {
//                    errors.add("Không tìm thấy ngày sinh của trẻ " + child.getFullname());
//                    continue;
//                }
//                LocalDate referenceDate = firstVaccinationDate != null ? firstVaccinationDate.toLocalDate() : LocalDate.now();
//                Period age = Period.between(dob, referenceDate);
//                int childAgeInMonths = age.getYears() * 12 + age.getMonths();
//
//                if (childAgeInMonths < product.getMinAgeMonths() || childAgeInMonths > product.getMaxAgeMonths()) {
//                    errors.add("Bé " + child.getFullname() + " không phù hợp với độ tuổi tiêm của vaccine \""
//                            + product.getTitle() + "\" (tuổi hiện tại: " + childAgeInMonths + " tháng).");
//                    continue;
//                }
//
//                // Kiểm tra ngày tiêm và khoảng cách giữa các mũi tiêm
//                if (firstVaccinationDate != null) {
//                    boolean alreadyScheduledSameVaccine = orderDetailRepository
//                            .existsByChildIdAndProductIdAndVaccinationDate(childId, productId, firstVaccinationDate);
//
//                    if (alreadyScheduledSameVaccine) {
//                        errors.add("Bé " + child.getFullname()
//                                + " đã có lịch tiêm vaccine \"" + product.getTitle() + "\" vào "
//                                + firstVaccinationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
//                                + ". Vui lòng chọn thời gian khác hoặc bỏ chọn vaccine này.");
//                        continue;
//                    }
//
//                    boolean isValidVaccinationDate = isValidVaccinationDate(firstVaccinationDate, product, child);
//                    if (!isValidVaccinationDate) {
//                        errors.add("Không thể đặt lịch vaccine \"" + product.getTitle() + "\" cho bé "
//                                + child.getFullname() + " vì chưa đủ thời gian giữa các mũi tiêm.");
//                        continue;
//                    }
//                }
//
//                // Tạo chi tiết đơn hàng
//                OrderDetail detail = new OrderDetail();
//                detail.setOrderId(order.getOrderId());
//                detail.setProduct(product);
//                detail.setChild(child);
//                detail.setQuantity(1);
//                detail.setStatus(OrderDetailStatus.CHUA_TIEM);
//                detail.setFirstName(orderRequest.getFirstName());
//                detail.setLastName(orderRequest.getLastName());
//                detail.setEmail(orderRequest.getEmail());
//                detail.setMobileNo(orderRequest.getMobileNo());
//
//                if (firstVaccinationDate != null) {
//                    detail.setVaccinationDate(firstVaccinationDate);
//                }
//
//                orderDetails.add(detail);
//                totalPrice += product.getPrice();
//            }
//        }
//
//        // Nếu có lỗi, ném exception với tất cả lỗi gặp phải
//        if (!errors.isEmpty()) {
//            throw new IllegalArgumentException("Không thể tạo đơn hàng vì lỗi:\n" + String.join("\n", errors));
//        }
//
//        // Lưu các chi tiết đơn hàng và cập nhật lại số lượng đã đặt
//        orderDetailRepository.saveAll(orderDetails);
//        productRepository.saveAll(orderDetails.stream().map(OrderDetail::getProduct).distinct().toList());
//        order.setTotalPrice(totalPrice);
//        productOrderRepository.save(order);
//
//        return order;  // Trả về đơn hàng vừa tạo
//    }

    //===============================================3

    @Override
    @Transactional
    public ProductOrder createOrderByStaff(Long parentId, Map<Long, List<Long>> childProductMap, OrderRequest orderRequest) {
        // Lấy thông tin staff đăng nhập
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long staffId = jwt.getClaim("id");

        List<String> errors = new ArrayList<>();
        List<OrderDetail> orderDetails = new ArrayList<>();
        double totalPrice = 0.0;

        // Kiểm tra parent
        User parent = userRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phụ huynh với ID: " + parentId));

        // Kiểm tra ngày tiêm
        LocalDateTime firstVaccinationDate = orderRequest.getVaccinationdate();
        if (firstVaccinationDate != null) {
            if (firstVaccinationDate.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Ngày tiêm mong muốn phải từ hôm nay trở đi.");
            }
            LocalTime time = firstVaccinationDate.toLocalTime();
            if (time.isBefore(LocalTime.of(7, 30)) || time.isAfter(LocalTime.of(17, 0))) {
                throw new IllegalArgumentException("Giờ tiêm chỉ được chọn từ 07:30 đến 17:00.");
            }
        }

        // Tạo đơn hàng
        ProductOrder order = new ProductOrder();
        order.setOrderId("ORD" + System.currentTimeMillis());
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.ORDER_RECEIVED.getName());
        order.setPaymentType("Trả tiền mặt tại hệ thống");
        order.setUser(parent);
        productOrderRepository.save(order);

        // Lặp qua từng trẻ và vaccine
        for (Map.Entry<Long, List<Long>> entry : childProductMap.entrySet()) {
            Long childId = entry.getKey();
            List<Long> productIds = entry.getValue().stream().distinct().toList();

            User child = userRepository.findByIdDirect(childId);
            if (child == null) {
                errors.add("Không tìm thấy trẻ với ID: " + childId);
                continue;
            }

            if (!Objects.equals(child.getParentid(), parentId)) {
                errors.add("Trẻ ID " + childId + " không thuộc phụ huynh ID " + parentId);
                continue;
            }

            // Kiểm tra từng vaccine cho trẻ
            for (Long productId : productIds) {
                Product product = productRepository.findById(productId).orElse(null);
                if (product == null) {
                    errors.add("Không tìm thấy sản phẩm ID: " + productId);
                    continue;
                }

                int available = product.getQuantity() - product.getReservedQuantity();
                if (available < 1) {
                    errors.add("Sản phẩm \"" + product.getTitle() + "\" không còn đủ số lượng cho bé " + child.getFullname());
                    continue;
                }

                // Duyệt qua các ProductDetails của sản phẩm để tìm lô có đủ số lượng và ngày hết hạn hợp lệ
                ProductDetails selectedProductDetail = null;
                int quantityRequested = 1; // Giả sử chỉ yêu cầu 1 sản phẩm cho ví dụ này
                for (ProductDetails pd : product.getProductDetails()) {
                    if (pd.getQuantity() - pd.getReservedQuantity() >= quantityRequested && pd.getExpirationDate().isAfter(firstVaccinationDate.toLocalDate())) {
                        selectedProductDetail = pd;
                        break;  // Nếu tìm được lô hợp lệ thì dừng lại
                    }
                }

                if (selectedProductDetail != null) {
                    // Lưu lại ProductDetail đã chọn trong OrderDetail
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrderId(order.getOrderId());
                    orderDetail.setProduct(product);
                    orderDetail.setChild(child);
                    orderDetail.setQuantity(1);  // Giả sử chỉ yêu cầu 1 sản phẩm
                    orderDetail.setStatus(OrderDetailStatus.CHUA_TIEM);  // Trạng thái ban đầu
                    orderDetail.setProductDetails(selectedProductDetail);  // Lưu lô đã chọn (chứa product_detail_id)
                    orderDetail.setFirstName(orderRequest.getFirstName());
                    orderDetail.setLastName(orderRequest.getLastName());
                    orderDetail.setEmail(orderRequest.getEmail());
                    orderDetail.setMobileNo(orderRequest.getMobileNo());
                    orderDetail.setVaccinationDate(firstVaccinationDate);  // Đặt ngày tiêm
                    // Tự động gán bác sĩ là user có role 'STAFF'
                    User doctor = getStaffUser();
                    orderDetail.setStaffid(doctor);  // Gán bác sĩ vào đơn hàng
                    orderDetail.setStaffName(doctor.getFullname());  // Gán tên bác sĩ tiêm vào OrderDetail



                    orderDetails.add(orderDetail);

                    // Cập nhật số lượng đã đặt cho sản phẩm và ProductDetail
                    selectedProductDetail.setReservedQuantity(selectedProductDetail.getReservedQuantity() + 1); // Cập nhật lô
                    product.setReservedQuantity(product.getReservedQuantity() + 1); // Cập nhật tổng reservedQuantity của sản phẩm

                    // Cập nhật tổng giá trị đơn hàng
                    totalPrice += product.getPrice();
                }
            }
        }

        // Nếu có lỗi, ném exception với tất cả lỗi gặp phải
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Không thể tạo đơn hàng vì lỗi:\n" + String.join("\n", errors));
        }

        // Lưu lại Order và OrderDetail
        productOrderRepository.save(order);
        orderDetailRepository.saveAll(orderDetails);
        order.setTotalPrice(totalPrice);
        productOrderRepository.save(order);

        return order;
    }

    private User getStaffUser() {
        // Tìm tất cả user có role STAFF
        List<User> staffUsers = userRepository.findByRoles_Name("STAFF");

        if (staffUsers != null && !staffUsers.isEmpty()) {
            int random= ThreadLocalRandom.current().nextInt(staffUsers.size());
            // Trả về user đầu tiên (bác sĩ tiêm) trong danh sách STAFF
            return staffUsers.get(random);
        } else {
            // Nếu không tìm thấy, ném một exception hoặc xử lý theo nhu cầu
            throw new IllegalArgumentException("Không tìm thấy bác sĩ tiêm.");
        }
    }







}

