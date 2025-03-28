package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.OrderRequest;
import com.example.SpringBootTurialVip.dto.response.OrderDetailResponse;
import com.example.SpringBootTurialVip.dto.response.ProductSuggestionResponse;
import com.example.SpringBootTurialVip.dto.response.UpcomingVaccinationResponse;
import com.example.SpringBootTurialVip.dto.response.VaccinationHistoryResponse;
import com.example.SpringBootTurialVip.entity.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductOrderRepository orderRepository;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CartRepository cartRepository;

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



    @Override
    public List<ProductOrder> getOrdersByUser(Long userId) {
        List<ProductOrder> orders = orderRepository.findByUserId(userId);
        return orders;
    }

    @Override
    public ProductOrder updateOrderStatus(Long id, String status) {
        Optional<ProductOrder> findById = orderRepository.findById(id);
        if (findById.isPresent()) {
            ProductOrder productOrder = findById.get();
            productOrder.setStatus(status);
            ProductOrder updateOrder = orderRepository.save(productOrder);
            // Gửi thông báo khi trạng thái đơn vaccine thay đổi
            notificationService.sendOrderStatusNotification(productOrder.getUser().getId(), status);
            return updateOrder;
        }
        return null;
    }

    @Override
    public List<ProductOrder> getAllOrders() {
        return productOrderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
    }

    @Override
    public ProductOrder getOrdersByOrderId(String orderId) {
        return null;
    }

    @Override
    public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
        return null;
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
    public ProductOrder getOrderById(Long orderId) {
        return productOrderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found with ID: " + orderId));
    }






//@Override
//@Transactional
//public ProductOrder createOrderByProductId(List<Long> productId, OrderRequest orderRequest) {
//    List<Product> selectedProducts = productRepository.findAllById(productId);
//
//    Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    Long userId = jwt.getClaim("id");
//    User parent = userRepository.findById(userId)
//            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
//
//    List<String> errors = new ArrayList<>();
//    List<OrderDetail> orderDetails = new ArrayList<>();
//    double totalPrice = 0.0;
//
//    // Kiểm tra ngày tiêm mong muốn
//    LocalDateTime vaccinationDate = orderRequest.getVaccinationdate();
//    if (vaccinationDate != null) {
//        if (vaccinationDate.isBefore(LocalDateTime.now())) {
//            throw new IllegalArgumentException("Ngày tiêm mong muốn phải từ hôm nay trở đi.");
//        }
//
//        LocalTime time = vaccinationDate.toLocalTime();
//        LocalTime start = LocalTime.of(7, 30);
//        LocalTime end = LocalTime.of(17, 0);
//        if (time.isBefore(start) || time.isAfter(end)) {
//            throw new IllegalArgumentException("Giờ tiêm chỉ được chọn từ 07:30 đến 17:00.");
//        }
//    }
//
//    ProductOrder order = new ProductOrder();
//    order.setOrderId("ORD" + System.currentTimeMillis());
//    order.setOrderDate(LocalDate.now());
//    order.setStatus(OrderStatus.ORDER_RECEIVED.getName());
//    order.setPaymentType("VNPAY");
//    order.setUser(parent);
//    productOrderRepository.save(order);
//
//    for (Long childId : orderRequest.getChildId()) {
//        User child = userRepository.findByIdDirect(childId);
//        if (child == null || !Objects.equals(child.getParentid(), userId)) {
//            errors.add("Trẻ ID " + childId + " không hợp lệ hoặc không thuộc phụ huynh.");
//            continue;
//        }
//
//        if (vaccinationDate != null) {
//            boolean exists = orderDetailRepository.existsByChildIdAndVaccinationDate(childId, vaccinationDate);
//            if (exists) {
//                errors.add("Bé " + child.getFullname()
//                        + " đã có lịch tiêm vào "
//                        + vaccinationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
//                        + ". Vui lòng chọn thời gian khác.");
//                continue;
//            }
//        }
//
//
//        for (Product product : selectedProducts) {
//            int totalDosesRequired = product.getNumberOfDoses();
//            int systemDosesTaken = orderDetailRepository.countDosesTaken(product.getId(), child.getId());
////            int dosesRemaining = totalDosesRequired - systemDosesTaken - orderRequest.getDosesAlreadyTaken();
//            int dosesRemaining = totalDosesRequired - systemDosesTaken ;
////
//            if (dosesRemaining <= 0) {
//                errors.add("Bé " + child.getFullname() + " đã tiêm đủ số mũi vaccine \"" + product.getTitle() + "\".");
//                continue;
//            }
//
//            int available = product.getQuantity() - product.getReservedQuantity();
//            if (available < dosesRemaining) {
//                errors.add("Sản phẩm \"" + product.getTitle() + "\" không đủ số lượng cho bé " + child.getFullname()
//                        + ". Cần " + totalDosesRequired + ", còn lại " + available);
//                continue;
//            }
//
//            for (int i = 0; i < dosesRemaining; i++) {
//                OrderDetail detail = new OrderDetail();
//                detail.setOrderId(order.getOrderId());
//                detail.setProduct(product);
//                detail.setChild(child);
//                detail.setQuantity(1);
//
//                // Chỉ set ngày tiêm cho mũi đầu tiên
//                if (i == 0) {
//                    detail.setVaccinationDate(vaccinationDate);
//                } else {
//                    detail.setVaccinationDate(null);
//                }
//
//                detail.setStatus(OrderDetailStatus.CHUA_TIEM);
//                detail.setFirstName(orderRequest.getFirstName());
//                detail.setLastName(orderRequest.getLastName());
//                detail.setEmail(orderRequest.getEmail());
//                detail.setMobileNo(orderRequest.getMobileNo());
//
//                orderDetails.add(detail);
//                totalPrice += product.getPrice();
//            }
//
//            product.setReservedQuantity(product.getReservedQuantity() + dosesRemaining);
//        }
//    }
//
//    // Có lỗi => không lưu gì hết
//    if (!errors.isEmpty()) {
//        throw new IllegalArgumentException("Không thể tạo đơn hàng vì các lỗi sau:\n" + String.join("\n", errors));
//    }
//
//    orderDetailRepository.saveAll(orderDetails);
//    for (Product p : selectedProducts) {
//        productRepository.save(p);
//    }
//
//    order.setTotalPrice(totalPrice);
//    productOrderRepository.save(order);
//    return order;
//}
    //===============================================================================================================
//@Override
//@Transactional
//public ProductOrder createOrderByProductId(List<Long> productId, OrderRequest orderRequest) {
//    List<Product> selectedProducts = productRepository.findAllById(productId);
//
//    Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    Long userId = jwt.getClaim("id");
//    User parent = userRepository.findById(userId)
//            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
//
//    List<String> errors = new ArrayList<>();
//    List<OrderDetail> orderDetails = new ArrayList<>();
//    double totalPrice = 0.0;
//
//    // Ngày tiêm mong muốn của mũi đầu tiên
//    LocalDateTime firstVaccinationDate = orderRequest.getVaccinationdate();
//    if (firstVaccinationDate != null) {
//        if (firstVaccinationDate.isBefore(LocalDateTime.now())) {
//            throw new IllegalArgumentException("Ngày tiêm mong muốn phải từ hôm nay trở đi.");
//        }
//        LocalTime time = firstVaccinationDate.toLocalTime();
//        if (time.isBefore(LocalTime.of(7, 30)) || time.isAfter(LocalTime.of(17, 0))) {
//            throw new IllegalArgumentException("Giờ tiêm chỉ được chọn từ 07:30 đến 17:00.");
//        }
//    }
//
//    ProductOrder order = new ProductOrder();
//    order.setOrderId("ORD" + System.currentTimeMillis());
//    order.setOrderDate(LocalDate.now());
//    order.setStatus(OrderStatus.ORDER_RECEIVED.getName());
//    order.setPaymentType("VNPAY");
//    order.setUser(parent);
//    productOrderRepository.save(order);
//
//    for (Long childId : orderRequest.getChildId()) {
//        User child = userRepository.findByIdDirect(childId);
//        if (child == null || !Objects.equals(child.getParentid(), userId)) {
//            errors.add("Trẻ ID " + childId + " không hợp lệ hoặc không thuộc phụ huynh.");
//            continue;
//        }
//
//        // Check trùng lịch cho mũi đầu tiên
//        if (firstVaccinationDate != null) {
//            boolean exists = orderDetailRepository.existsByChildIdAndVaccinationDate(childId, firstVaccinationDate);
//            if (exists) {
//                errors.add("Bé " + child.getFullname()
//                        + " đã có lịch tiêm vào "
//                        + firstVaccinationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
//                        + ". Vui lòng chọn thời gian khác.");
//                continue;
//            }
//        }
//
//        for (Product product : selectedProducts) {
//            int totalDoses = product.getNumberOfDoses();
//            int dosesTaken = orderDetailRepository.countDosesTaken(product.getId(), childId);
//            int dosesRemaining = totalDoses - dosesTaken;
//
//            if (dosesRemaining <= 0) {
//                errors.add("Bé " + child.getFullname() + " đã tiêm đủ số mũi vaccine \"" + product.getTitle() + "\".");
//                continue;
//            }
//
//            int available = product.getQuantity() - product.getReservedQuantity();
//            if (available < dosesRemaining) {
//                errors.add("Sản phẩm \"" + product.getTitle() + "\" không đủ số lượng cho bé " + child.getFullname()
//                        + ". Cần " + dosesRemaining + ", còn lại " + available);
//                continue;
//            }
//
//            // Tính toán ngày tiêm cho các mũi
//            int minDaysBetween = Optional.ofNullable(product.getMinDaysBetweenDoses()).orElse(30);
//            for (int i = 0; i < dosesRemaining; i++) {
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
//                    // Tính ngày tiêm từng mũi
//                    LocalDateTime scheduledDate = firstVaccinationDate.plusDays((long) i * minDaysBetween);
//                    detail.setVaccinationDate(scheduledDate);
//                }
//
//                orderDetails.add(detail);
//                totalPrice += product.getPrice();
//            }
//
//            // Cộng thêm số mũi đã đặt
//            product.setReservedQuantity(product.getReservedQuantity() + dosesRemaining);
//        }
//    }
//
//    if (!errors.isEmpty()) {
//        throw new IllegalArgumentException("Không thể tạo đơn hàng vì lỗi:\n" + String.join("\n", errors));
//    }
//
//    orderDetailRepository.saveAll(orderDetails);
//    selectedProducts.forEach(productRepository::save);
//    order.setTotalPrice(totalPrice);
//    productOrderRepository.save(order);
//
//    return order;
//}
//
//    @Override
//    public List<ProductOrder> getOrdersByStatus(String status) {
//        return orderRepository.findByStatus(status);
//    }


//    @Override
//    @Transactional
//    public ProductOrder createOrderByProductIdByStaff(Long userId,
//                                                      List<Long> productId,
//                                                      OrderRequest orderRequest) {
//
//        List<Product> selectedProducts = productRepository.findAllById(productId);
//        User parent = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
//
//        // Gom lỗi tại đây
//        List<String> errors = new ArrayList<>();
//        List<OrderDetail> orderDetails = new ArrayList<>();
//        double totalPrice = 0.0;
//
//        // Kiểm tra ngày tiêm mong muốn
//        LocalDateTime vaccinationDate = orderRequest.getVaccinationdate();
//        if (vaccinationDate != null) {
//            if (vaccinationDate.isBefore(LocalDateTime.now())) {
//                throw new IllegalArgumentException("Ngày tiêm mong muốn phải từ hôm nay trở đi.");
//            }
//
//            LocalTime time = vaccinationDate.toLocalTime();
//            LocalTime start = LocalTime.of(7, 30);
//            LocalTime end = LocalTime.of(17, 0);
//            if (time.isBefore(start) || time.isAfter(end)) {
//                throw new IllegalArgumentException("Giờ tiêm chỉ được chọn từ 07:30 đến 17:00.");
//            }
//        }
//
//
//        ProductOrder order = new ProductOrder();
//        order.setOrderId("ORD" + System.currentTimeMillis());
//        order.setOrderDate(LocalDate.now());
//        order.setStatus(OrderStatus.ORDER_RECEIVED.getName());
//        order.setPaymentType("VNPAY");
//        order.setUser(parent);
//        productOrderRepository.save(order); // vẫn tạo trước
//
//        for (Long childId : orderRequest.getChildId()) {
//            User child = userRepository.findByIdDirect(childId);
//            if (child == null || !Objects.equals(child.getParentid(), userId)) {
//                errors.add("Trẻ ID " + childId + " không hợp lệ hoặc không thuộc phụ huynh.");
//                continue;
//            }
//
//            if (vaccinationDate != null) {
//                boolean exists = orderDetailRepository.existsByChildIdAndVaccinationDate(childId, vaccinationDate);
//                if (exists) {
//                    errors.add("Bé " + child.getFullname()
//                            + " đã có lịch tiêm vào "
//                            + vaccinationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
//                            + ". Vui lòng chọn thời gian khác.");
//                    continue;
//                }
//            }
//
//
//            for (Product product : selectedProducts) {
//                int totalDosesRequired = product.getNumberOfDoses();
//                int systemDosesTaken = orderDetailRepository.countDosesTaken(product.getId(), child.getId());
////                int dosesRemaining = totalDosesRequired - systemDosesTaken - orderRequest.getDosesAlreadyTaken();
//                int dosesRemaining = totalDosesRequired - systemDosesTaken ;
//
//                if (dosesRemaining <= 0) {
//                    errors.add("Bé " + child.getFullname() + " đã tiêm đủ số mũi vaccine \"" + product.getTitle() + "\".");
//                    continue;
//                }
//
//                int available = product.getQuantity() - product.getReservedQuantity();
//                if (available < dosesRemaining) {
//                    errors.add("Sản phẩm \"" + product.getTitle() + "\" không đủ số lượng cho bé " + child.getFullname()
//                            + ". Cần " + dosesRemaining + ", còn lại " + available);
//                    continue;
//                }
//
//                for (int i = 0; i < dosesRemaining; i++) {
//                    OrderDetail detail = new OrderDetail();
//                    detail.setOrderId(order.getOrderId());
//                    detail.setProduct(product);
//                    detail.setChild(child);
//                    detail.setQuantity(1);
//
//                    // Chỉ set ngày tiêm cho mũi đầu tiên
//                    if (i == 0) {
//                        detail.setVaccinationDate(vaccinationDate);
//                    } else {
//                        detail.setVaccinationDate(null);
//                    }
//
//                    detail.setVaccinationDate(orderRequest.getVaccinationdate());
//                    detail.setStatus(OrderDetailStatus.CHUA_TIEM);
//                    detail.setFirstName(orderRequest.getFirstName());
//                    detail.setLastName(orderRequest.getLastName());
//                    detail.setEmail(orderRequest.getEmail());
//                    detail.setMobileNo(orderRequest.getMobileNo());
//
//                    orderDetails.add(detail);
//                    totalPrice += product.getPrice();
//                }
//
//                product.setReservedQuantity(product.getReservedQuantity() + dosesRemaining);
//            }
//        }
//
//        //Nếu có lỗi => hủy đơn + trả lỗi
//        if (!errors.isEmpty()) {
//            throw new IllegalArgumentException("Không thể tạo đơn hàng vì các lỗi sau:\n" + String.join("\n", errors));
//        }
//
//        // Lưu dữ liệu nếu không có lỗi
//        orderDetailRepository.saveAll(orderDetails);
//        for (Product p : selectedProducts) {
//            productRepository.save(p);
//        }
//
//        order.setTotalPrice(totalPrice);
//        productOrderRepository.save(order);
//        return order;
//    }
//@Override
//@Transactional
//public ProductOrder createOrderByProductIdByStaff(Long userId,
//                                                  List<Long> productId,
//                                                  OrderRequest orderRequest) {
//
//    List<Product> selectedProducts = productRepository.findAllById(productId);
//    User parent = userRepository.findById(userId)
//            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
//
//    List<String> errors = new ArrayList<>();
//    List<OrderDetail> orderDetails = new ArrayList<>();
//    double totalPrice = 0.0;
//
//    LocalDateTime firstVaccinationDate = orderRequest.getVaccinationdate();
//    if (firstVaccinationDate != null) {
//        if (firstVaccinationDate.isBefore(LocalDateTime.now())) {
//            throw new IllegalArgumentException("Ngày tiêm mong muốn phải từ hôm nay trở đi.");
//        }
//
//        LocalTime time = firstVaccinationDate.toLocalTime();
//        if (time.isBefore(LocalTime.of(7, 30)) || time.isAfter(LocalTime.of(17, 0))) {
//            throw new IllegalArgumentException("Giờ tiêm chỉ được chọn từ 07:30 đến 17:00.");
//        }
//    }
//
//    ProductOrder order = new ProductOrder();
//    order.setOrderId("ORD" + System.currentTimeMillis());
//    order.setOrderDate(LocalDate.now());
//    order.setStatus(OrderStatus.ORDER_RECEIVED.getName());
//    order.setPaymentType("VNPAY");
//    order.setUser(parent);
//    productOrderRepository.save(order);
//
//    for (Long childId : orderRequest.getChildId()) {
//        User child = userRepository.findByIdDirect(childId);
//        if (child == null || !Objects.equals(child.getParentid(), userId)) {
//            errors.add("Trẻ ID " + childId + " không hợp lệ hoặc không thuộc phụ huynh.");
//            continue;
//        }
//
//        if (firstVaccinationDate != null) {
//            boolean exists = orderDetailRepository.existsByChildIdAndVaccinationDate(childId, firstVaccinationDate);
//            if (exists) {
//                errors.add("Bé " + child.getFullname()
//                        + " đã có lịch tiêm vào "
//                        + firstVaccinationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
//                        + ". Vui lòng chọn thời gian khác.");
//                continue;
//            }
//        }
//
//        for (Product product : selectedProducts) {
//            int totalDoses = product.getNumberOfDoses();
//            int dosesTaken = orderDetailRepository.countDosesTaken(product.getId(), childId);
//            int dosesRemaining = totalDoses - dosesTaken;
//
//            if (dosesRemaining <= 0) {
//                errors.add("Bé " + child.getFullname() + " đã tiêm đủ số mũi vaccine \"" + product.getTitle() + "\".");
//                continue;
//            }
//
//            int available = product.getQuantity() - product.getReservedQuantity();
//            if (available < dosesRemaining) {
//                errors.add("Sản phẩm \"" + product.getTitle() + "\" không đủ số lượng cho bé " + child.getFullname()
//                        + ". Cần " + dosesRemaining + ", còn lại " + available);
//                continue;
//            }
//
//            int minDaysBetween = Optional.ofNullable(product.getMinDaysBetweenDoses()).orElse(30);
//            for (int i = 0; i < dosesRemaining; i++) {
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
//                    LocalDateTime scheduledDate = firstVaccinationDate.plusDays((long) i * minDaysBetween);
//                    detail.setVaccinationDate(scheduledDate);
//                }
//
//                orderDetails.add(detail);
//                totalPrice += product.getPrice();
//            }
//
//            product.setReservedQuantity(product.getReservedQuantity() + dosesRemaining);
//        }
//    }
//
//    if (!errors.isEmpty()) {
//        throw new IllegalArgumentException("Không thể tạo đơn hàng vì các lỗi sau:\n" + String.join("\n", errors));
//    }
//
//    orderDetailRepository.saveAll(orderDetails);
//    selectedProducts.forEach(productRepository::save);
//    order.setTotalPrice(totalPrice);
//    productOrderRepository.save(order);
//
//    return order;
//}





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
        return orderDetailRepository.getVaccinationHistory(childId);
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
//        OrderDetail orderDetail = orderDetailRepository.findById(Math.toIntExact(orderDetailId))
//                .orElseThrow(() -> new RuntimeException("OrderDetail not found"));
//
//        // Cập nhật trạng thái của OrderDetail
//        orderDetail.setStatus(newStatus);
//        orderDetailRepository.save(orderDetail);
//
//        // Lấy order_id từ orderDetail
//        String orderId = orderDetail.getOrderId(); // Giả sử OrderDetail có trường orderId
//
//        // Kiểm tra nếu tất cả OrderDetail của order_id đã có trạng thái "Đã tiêm chủng"
//        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
//        boolean allVaccinated = orderDetails.stream()
//                .allMatch(detail -> detail.getStatus() == OrderDetailStatus.DA_TIEM);
//
//        if (allVaccinated) {
//            // Cập nhật trạng thái ProductOrder thành SUCCESS
//            ProductOrder productOrder = productOrderRepository.findByOrderId(orderId);
//            if (productOrder == null) {
//                throw new RuntimeException("ProductOrder not found");
//            }
//
//            productOrder.setStatus(String.valueOf(OrderStatus.SUCCESS));
//            productOrderRepository.save(productOrder);
//        }
//    }
@Override
@Transactional
public void updateOrderDetailStatus(Long orderDetailId, OrderDetailStatus status) {
    OrderDetail detail = orderDetailRepository.findById(Math.toIntExact(orderDetailId))
            .orElseThrow(() -> new NoSuchElementException("Không tìm thấy OrderDetail với ID: " + orderDetailId));

    // Cập nhật trạng thái
    detail.setStatus(status);
    orderDetailRepository.save(detail);

    // ✅ Nếu là mũi đã tiêm -> cập nhật tồn kho và reserved
    if (status == OrderDetailStatus.DA_TIEM) {
        Product product = detail.getProduct();
        int qty = detail.getQuantity();

        // Trừ tồn kho và giải phóng số đã giữ
        product.setQuantity(product.getQuantity() - qty);
        product.setReservedQuantity(product.getReservedQuantity() - qty);
        productRepository.save(product);
    }

    // ✅ Kiểm tra nếu tất cả mũi trong đơn đều đã tiêm -> cập nhật ProductOrder thành SUCCESS
    String orderId = detail.getOrderId();
    List<OrderDetail> allDetails = orderDetailRepository.findByOrderId(orderId);
    boolean allDone = allDetails.stream()
            .allMatch(d -> d.getStatus() == OrderDetailStatus.DA_TIEM);

    ProductOrder order = productOrderRepository.findByOrderId(orderId);
    if (order != null && allDone) {
        order.setStatus(OrderStatus.SUCCESS.getName());
        productOrderRepository.save(order);
    } else if (order == null) {
        log.warn("Không tìm thấy ProductOrder với orderId: " + orderId);
    }
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
                od.getChild().getId()
        )).collect(Collectors.toList());
    }

    @Override
    public List<ProductSuggestionResponse> suggestVaccinesForChild(Long childId) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");

        User child = userRepository.findByIdDirect(childId);
        if (child == null || !Objects.equals(child.getParentid(), userId)) {
            throw new IllegalArgumentException("Đây không phải là trẻ của bạn");
        }

        int ageInMonths = Period.between(child.getBod(), LocalDate.now()).getYears() * 12
                + Period.between(child.getBod(), LocalDate.now()).getMonths();

        List<Product> allProducts = productRepository.findAll();

        return allProducts.stream()
                .filter(product -> {
                    Integer minAge = product.getMinAgeMonths();
                    Integer maxAge = product.getMaxAgeMonths();
                    boolean ageValid = (minAge == null || ageInMonths >= minAge)
                            && (maxAge == null || ageInMonths <= maxAge);
                    return ageValid;
                })
                .filter(product -> {
                    int taken = orderDetailRepository.countDosesTaken(product.getId(), childId);
                    Integer numberOfDoses = product.getNumberOfDoses();
                    return numberOfDoses != null && taken < numberOfDoses;
                })
                .filter(product -> {
                    Integer quantity = product.getQuantity();
                    Integer reserved = product.getReservedQuantity();
                    return quantity != null && reserved != null && (quantity - reserved) > 0;
                })
                .sorted(Comparator.comparing(Product::getIsPriority, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(product -> new ProductSuggestionResponse(
                        product.getId(),
                        product.getTitle(),
                        product.getDescription(),
                        product.getImage(),
                        product.getPrice(),
                        product.getDiscountPrice(),
                        product.getIsPriority(),
                        product.getManufacturer(),               // nguồn gốc
                        product.getMinAgeMonths(),        // tuổi tối thiểu
                        product.getMaxAgeMonths(),        // tuổi tối đa
                        product.getNumberOfDoses()        // số mũi
                ))

                .toList();
    }

//@Override
//public List<Product> suggestVaccinesForChild(Long childId) {
//    // Lấy user đang login
//    Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    Long userId = jwt.getClaim("id");
//
//    // Kiểm tra quyền truy cập
//    User child = userRepository.findByIdDirect(childId);
//    if (child == null || !Objects.equals(child.getParentid(), userId)) {
//        throw new IllegalArgumentException("Đây không phải là trẻ của bạn");
//    }
//
//    // Tính tuổi theo tháng
//    int ageInMonths = Period.between(child.getBod(), LocalDate.now()).getYears() * 12
//            + Period.between(child.getBod(), LocalDate.now()).getMonths();
//
//    // Lấy toàn bộ sản phẩm từ DB (bao gồm cả các trường minAge, maxAge, priorityTag...)
//    List<Product> allProducts = productRepository.findAll();
//
//    return allProducts.stream()
//            // Kiểm tra độ tuổi phù hợp
//            .filter(product -> {
//                Integer minAge = product.getMinAgeMonths();
//                Integer maxAge = product.getMaxAgeMonths();
//                boolean ageValid = (minAge == null || ageInMonths >= minAge)
//                        && (maxAge == null || ageInMonths <= maxAge);
//                return ageValid;
//            })
//            // Kiểm tra số mũi còn lại
//            .filter(product -> {
//                int taken = orderDetailRepository.countDosesTaken(product.getId(), childId);
//                Integer numberOfDoses = product.getNumberOfDoses();
//                return numberOfDoses != null && taken < numberOfDoses;
//            })
//            // Kiểm tra còn hàng
//            .filter(product -> {
//                Integer quantity = product.getQuantity();
//                Integer reserved = product.getReservedQuantity();
//                return quantity != null && reserved != null && (quantity - reserved) > 0;
//            })
//            // Sắp xếp theo tag ưu tiên (true lên trước)
//            .sorted(Comparator.comparing(Product::getIsPriority, Comparator.nullsLast(Comparator.reverseOrder())))
//            .toList();
//}


    @Override
    public List<ProductSuggestionResponse> suggestVaccinesByStaff(Long childId) {

        User child = userRepository.findByIdDirect(childId);

        int ageInMonths = Period.between(child.getBod(), LocalDate.now()).getYears() * 12
                + Period.between(child.getBod(), LocalDate.now()).getMonths();

        List<Product> suitable = productRepository.findSuitableProductsForAge(ageInMonths);

        return suitable.stream()
                .filter(product -> {
                    int taken = orderDetailRepository.countDosesTaken(product.getId(), childId);
                    int remaining = product.getNumberOfDoses() - taken;
                    return remaining > 0;
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
                        product.getNumberOfDoses()
                ))
                .toList();
    }


    @Override
    @Transactional
    public void cancelOrderByCustomer(String orderId, Long userId) throws AccessDeniedException {
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



        if (hasInjected) {
            throw new IllegalStateException("Không thể hủy đơn vì đã có mũi đã tiêm.");
        }

        // Cập nhật trạng thái đơn hàng
        order.setStatus(OrderStatus.CANCEL.getName());
        productOrderRepository.save(order);

        for (OrderDetail detail : details) {
            detail.setStatus(OrderDetailStatus.DA_HUY);
            detail.setVaccinationDate(null);

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
        ProductOrder order = productOrderRepository.findByOrderId(orderId);
        if (order == null) {
            throw new NoSuchElementException("Không tìm thấy đơn hàng");
        }

        List<OrderDetail> details = orderDetailRepository.findByOrderId(orderId);

        boolean hasInjected = details.stream()
                .anyMatch(d -> OrderDetailStatus.DA_TIEM.equals(d.getStatus()));

        // Nếu đã tiêm 1 phần -> chỉ hủy phần còn lại
        if (hasInjected) {
            for (OrderDetail detail : details) {
                if (!OrderDetailStatus.DA_TIEM.equals(detail.getStatus())) {
                    detail.setStatus(OrderDetailStatus.DA_HUY);
                    //detail.setVaccinationDate(null);

                    Product p = detail.getProduct();
                    p.setReservedQuantity(p.getReservedQuantity() - 1);
                    productRepository.save(p);
                }
            }

            order.setStatus(OrderStatus.CANCELED_PARTIAL.name());
        } else {
            // Hủy toàn bộ
            for (OrderDetail detail : details) {
                detail.setStatus(OrderDetailStatus.DA_HUY);
                detail.setVaccinationDate(null);

                Product p = detail.getProduct();
                p.setReservedQuantity(p.getReservedQuantity() - 1);
                productRepository.save(p);
            }

            order.setStatus(OrderStatus.CANCEL.name());
        }

        // Lưu lý do hủy nếu có
        if (reason != null && !reason.trim().isEmpty()) {
            order.setCancellationReason(reason);
        }

        productOrderRepository.save(order);
        orderDetailRepository.saveAll(details);

        // Gửi mail xác nhận
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
                od.getStatus().name()
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
                od.getProduct().getDiscountPrice(),
                od.getFirstName(),
                od.getLastName(),
                od.getEmail(),
                od.getMobileNo(),
                od.getStatus().name(),
                od.getChild().getFullname(),
                od.getChild().getId()
        )).toList();
    }

    @Override
    @Transactional
    public ProductOrder createOrderByChildProductMap(Map<Long, List<Long>> childProductMap, OrderRequest orderRequest) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

        ProductOrder order = new ProductOrder();
        order.setOrderId("ORD" + System.currentTimeMillis());
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.ORDER_RECEIVED.getName());
        order.setPaymentType("VNPAY");
        order.setUser(parent);
        productOrderRepository.save(order);

        for (Map.Entry<Long, List<Long>> entry : childProductMap.entrySet()) {
            Long childId = entry.getKey();
            List<Long> productIds = entry.getValue();

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

                int totalDoses = product.getNumberOfDoses();
                int dosesTaken = orderDetailRepository.countDosesTaken(productId, childId);
                int dosesRemaining = totalDoses - dosesTaken;

                if (dosesRemaining <= 0) {
                    errors.add("Bé " + child.getFullname() + " đã tiêm đủ số mũi vaccine \"" + product.getTitle() + "\".");
                    continue;
                }

                int available = product.getQuantity() - product.getReservedQuantity();
                if (available < dosesRemaining) {
                    errors.add("Sản phẩm \"" + product.getTitle() + "\" không đủ số lượng cho bé " + child.getFullname()
                            + ". Cần " + dosesRemaining + ", còn lại " + available);
                    continue;
                }

                if (firstVaccinationDate != null) {
                    boolean exists = orderDetailRepository.existsByChildIdAndVaccinationDate(childId, firstVaccinationDate);
                    if (exists) {
                        errors.add("Bé " + child.getFullname()
                                + " đã có lịch tiêm vào "
                                + firstVaccinationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                                + ". Vui lòng chọn thời gian khác.");
                        continue;
                    }
                }

                int minDaysBetween = Optional.ofNullable(product.getMinDaysBetweenDoses()).orElse(30);
                for (int i = 0; i < dosesRemaining; i++) {
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

                    if (firstVaccinationDate != null) {
                        LocalDateTime scheduledDate = firstVaccinationDate.plusDays((long) i * minDaysBetween);
                        detail.setVaccinationDate(scheduledDate);
                    }

                    orderDetails.add(detail);
                    totalPrice += product.getPrice();
                }

                product.setReservedQuantity(product.getReservedQuantity() + dosesRemaining);
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Không thể tạo đơn hàng vì lỗi:\n" + String.join("\n", errors));
        }

        orderDetailRepository.saveAll(orderDetails);
        productRepository.saveAll(orderDetails.stream().map(OrderDetail::getProduct).distinct().toList());
        order.setTotalPrice(totalPrice);
        productOrderRepository.save(order);

        return order;
    }

    @Override
    @Transactional
    public ProductOrder createOrderByStaff(Long parentId, Map<Long, List<Long>> childProductMap, OrderRequest orderRequest) {
        // Lấy thông tin staff đang login (không dùng ở đây nhưng có thể log hoặc lưu log nếu cần)
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long staffId = jwt.getClaim("id");

        List<String> errors = new ArrayList<>();
        List<OrderDetail> orderDetails = new ArrayList<>();
        double totalPrice = 0.0;

        // Kiểm tra parentId hợp lệ
        User parent = userRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phụ huynh với ID: " + parentId));

        // Kiểm tra ngày tiêm hợp lệ
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
        order.setPaymentType("VNPAY");
        order.setUser(parent); // Gán phụ huynh vào đơn
        productOrderRepository.save(order);

        // Lặp qua từng trẻ
        for (Map.Entry<Long, List<Long>> entry : childProductMap.entrySet()) {
            Long childId = entry.getKey();
            List<Long> productIds = entry.getValue().stream().distinct().toList();

            User child = userRepository.findByIdDirect(childId);
            if (child == null) {
                errors.add("Không tìm thấy trẻ với ID: " + childId);
                continue;
            }

            // Kiểm tra trẻ có thuộc phụ huynh không
            if (!Objects.equals(child.getParentid(), parentId)) {
                errors.add("Trẻ ID " + childId + " không thuộc phụ huynh ID " + parentId);
                continue;
            }

            for (Long productId : productIds) {
                Product product = productRepository.findById(productId).orElse(null);
                if (product == null) {
                    errors.add("Không tìm thấy sản phẩm ID: " + productId);
                    continue;
                }

                int totalDoses = product.getNumberOfDoses();
                int dosesTaken = orderDetailRepository.countDosesTaken(productId, childId);
                int dosesRemaining = totalDoses - dosesTaken;

                if (dosesRemaining <= 0) {
                    errors.add("Bé " + child.getFullname() + " đã tiêm đủ số mũi vaccine \"" + product.getTitle() + "\".");
                    continue;
                }

                int available = product.getQuantity() - product.getReservedQuantity();
                if (available < dosesRemaining) {
                    errors.add("Sản phẩm \"" + product.getTitle() + "\" không đủ số lượng cho bé " + child.getFullname()
                            + ". Cần " + dosesRemaining + ", còn lại " + available);
                    continue;
                }

                if (firstVaccinationDate != null) {
                    boolean exists = orderDetailRepository.existsByChildIdAndVaccinationDate(childId, firstVaccinationDate);
                    if (exists) {
                        errors.add("Bé " + child.getFullname()
                                + " đã có lịch tiêm vào "
                                + firstVaccinationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                                + ". Vui lòng chọn thời gian khác.");
                        continue;
                    }
                }

                int minDaysBetween = Optional.ofNullable(product.getMinDaysBetweenDoses()).orElse(30);
                for (int i = 0; i < dosesRemaining; i++) {
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

                    if (firstVaccinationDate != null) {
                        LocalDateTime scheduledDate = firstVaccinationDate.plusDays((long) i * minDaysBetween);
                        detail.setVaccinationDate(scheduledDate);
                    }

                    orderDetails.add(detail);
                    totalPrice += product.getPrice();
                }

                product.setReservedQuantity(product.getReservedQuantity() + dosesRemaining);
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Không thể tạo đơn hàng vì lỗi:\n" + String.join("\n", errors));
        }

        orderDetailRepository.saveAll(orderDetails);
        productRepository.saveAll(orderDetails.stream().map(OrderDetail::getProduct).distinct().toList());
        order.setTotalPrice(totalPrice);
        productOrderRepository.save(order);

        return order;
    }




// Nếu chưa tiêm mũi nào → hủy toàn bộ như bình thường


//    @Override
//    public List<VaccinationHistoryResponse> getCustomerVaccinationHistory(Long customerId) {
//        return orderRepository.getVaccinationHistoryByCustomerId(customerId);
//    }


//    @Override
//    public List<ProductOrder> getOrdersByStatusId(Integer statusId) {
//        return orderRepository.findByStatusId(statusId);
//    }

//    @Override
//    public void saveOrderByProductId(Long productId, OrderRequest orderRequest, Long userId) {
//        // Tìm sản phẩm theo ID
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + productId));
//
//        // Tìm user theo ID
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
//
//        // Kiểm tra số lượng sản phẩm trong kho
//        if (product.getStock() < orderRequest.getQuantity()) {
//            throw new IllegalStateException("Not enough stock available for product ID: " + productId);
//        }
//
//        // Trừ sản phẩm trong kho
//        product.setStock(product.getStock() - orderRequest.getQuantity());
//        productRepository.save(product);
//
//        // Tạo đơn hàng mới
//        ProductOrder order = new ProductOrder();
//        order.setOrderId(UUID.randomUUID().toString()); // Tạo ID đơn hàng ngẫu nhiên
//        order.setProduct(product);
//        order.setPrice(product.getPrice() * orderRequest.getQuantity());
//        order.setQuantity(orderRequest.getQuantity());
//        order.setStatus("Order Received");
//        order.setPaymentType(orderRequest.getPaymentType());
//        order.setUser(user);
//        order.setOrderDate(LocalDateTime.now());
//
//        // Lưu vào database
//        productOrderRepository.save(order);
//    }


    }

