package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.OrderRequest;
import com.example.SpringBootTurialVip.dto.response.OrderDetailResponse;
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

//    @Override
//    public void saveOrder(Long cartId, OrderRequest orderRequest) throws Exception {
//        // Tìm giỏ hàng theo cartId
//        Cart cart = cartRepository.findById(cartId)
//                .orElseThrow(() -> new NoSuchElementException("Cart với ID " + cartId + " không tồn tại"));
//
//
//
//        // Lưu thông tin địa chỉ đơn hàng
//        OrderDetail orderDetail = new OrderDetail();
//        orderDetail.setFirstName(orderRequest.getFirstName());
//        orderDetail.setLastName(orderRequest.getLastName());
//        orderDetail.setEmail(orderRequest.getEmail());
//        orderDetail.setMobileNo(orderRequest.getMobileNo());
//
//        //Convert từ childId qua type user
//        User child=userRepository.findByIdDirect(orderRequest.getChildId());
//        orderDetail.setChild(child);
//
//        // Tạo đơn hàng từ giỏ hàng
//        ProductOrder order = new ProductOrder();
//        order.setOrderId("ORD" + System.currentTimeMillis());
//        order.setOrderDate(LocalDate.now());
//       // order.setProduct(cart.getProduct());
//       // order.setPrice(cart.getProduct().getDiscountPrice());
//       // order.setQuantity(cart.getQuantity());
//        order.setUser(cart.getUser());
//        order.setStatus(OrderStatus.IN_PROGRESS.getName());
//        order.setPaymentType(orderRequest.getPaymentType());
//
//        //order.setOrderDetail(orderDetail);
//
//        // Lưu đơn hàng vào database
//        ProductOrder savedOrder = orderRepository.save(order);
//
//        // Gửi email xác nhận đơn hàng
//        commonUtil.sendMailForProductOrder(savedOrder, "success");
//
//        // Xóa sản phẩm khỏi giỏ hàng sau khi đặt hàng thành công
//        cartRepository.delete(cart);
//    }


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
        return productOrderRepository.findAll();
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

//    @Override
//    @Transactional//Nếu 1 bc trong db bị lỗi sẽ rollback tránh thừa thiếu dữ liệu ko xác định
//    public ProductOrder createOrderByProductId(List<Long> productId,
//                                               List<Integer> quantity,
//                                               OrderRequest orderRequest) {
//        // Lấy thông tin sản phẩm
////        Product product = productRepository.findById(productId)
////                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
//        // Lấy danh sách sản phẩm theo danh sách ID
//        List<Product> selectedProducts = productRepository.findAllById(productId);
//
//        // Lấy thông tin user từ JWT
//        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long userId = jwt.getClaim("id");
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
//
//        // Tạo đơn hàng mới
//        ProductOrder order = new ProductOrder();
//        order.setOrderId("ORD" + System.currentTimeMillis()); // Tạo mã đơn hàng
//        order.setOrderDate(LocalDate.now());
////        order.setProduct(product);
////        order.setPrice(product.getPrice() * quantity);
////        order.setQuantity(quantity);
//        order.setStatus(OrderStatus.ORDER_RECEIVED.getName()); // Trạng thái mặc định là "Order Received"
//        order.setPaymentType("VNPAY");
//        order.setUser(user);
////        order.setOrderDetail(orderDetail); // Gán thông tin chi tiết đơn hàng
//        productOrderRepository.save(order);
//
//        double totalPrice = 0.0;
//        List<OrderDetail> orderDetails = new ArrayList<>();
//
//        for (int i = 0; i < selectedProducts.size(); i++) {
//            Product product = selectedProducts.get(i);
//            int quantiti = quantity.get(i); // Số lượng mũi tiêm
//
//            for (int dose = 1; dose <= quantiti; dose++) {
//                OrderDetail orderDetail = new OrderDetail();
//                orderDetail.setOrderId(order.getOrderId()); // Gán orderId từ ProductOrder
//                orderDetail.setEmail(orderRequest.getEmail());
//                orderDetail.setFirstName(orderRequest.getFirstName());
//                orderDetail.setLastName(orderRequest.getLastName());
//                orderDetail.setMobileNo(orderRequest.getMobileNo());
//
//                //Bắt lỗi nếu trẻ không tồn tại
//                User child=userRepository.findByIdDirect(orderRequest.getChildId());
//                if (child == null) {
//                    throw new IllegalArgumentException("Trẻ có ID " + orderRequest.getChildId() + " không tồn tại.");
//                } else if (child.getParentid() != userId) {
//                    throw new IllegalArgumentException("Trẻ có ID "+orderRequest.getChildId()+" không phải là trẻ của bạn");
//                }
//                orderDetail.setChild(child);
//
//                orderDetail.setProduct(product);
//                orderDetail.setQuantity(1); // Mỗi OrderDetail chỉ lưu 1 mũi tiêm
//                orderDetail.setVaccinationDate(null); // Staff sẽ cập nhật sau
//
//                // **Lấy đúng giá trị sản phẩm**
//                double orderDetailPrice = product.getPrice(); // **Lấy giá đúng từ Product**                l
//                totalPrice += orderDetailPrice; // **Cộng giá vào tổng giá đơn hàng**
//
//                orderDetails.add(orderDetail);
//            }
//        }
//        // Lưu danh sách OrderDetail sau khi có ProductOrder**
//        orderDetailRepository.saveAll(orderDetails);
//
//        // Cập nhật tổng giá trị đơn hàng**
//        order.setTotalPrice(totalPrice);
//        productOrderRepository.save(order); // Cập nhật totalPrice
//
//        return order;
//    }
@Override
@Transactional
public ProductOrder createOrderByProductId(List<Long> productId, OrderRequest orderRequest) {
    List<Product> selectedProducts = productRepository.findAllById(productId);

    Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = jwt.getClaim("id");
    User parent = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

    List<String> errors = new ArrayList<>();
    List<OrderDetail> orderDetails = new ArrayList<>();
    double totalPrice = 0.0;

    // Kiểm tra ngày tiêm mong muốn
    LocalDateTime vaccinationDate = orderRequest.getVaccinationdate();
    if (vaccinationDate != null) {
        if (vaccinationDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Ngày tiêm mong muốn phải từ hôm nay trở đi.");
        }

        LocalTime time = vaccinationDate.toLocalTime();
        LocalTime start = LocalTime.of(7, 30);
        LocalTime end = LocalTime.of(17, 0);
        if (time.isBefore(start) || time.isAfter(end)) {
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

    for (Long childId : orderRequest.getChildId()) {
        User child = userRepository.findByIdDirect(childId);
        if (child == null || !Objects.equals(child.getParentid(), userId)) {
            errors.add("Trẻ ID " + childId + " không hợp lệ hoặc không thuộc phụ huynh.");
            continue;
        }

        if (vaccinationDate != null) {
            boolean exists = orderDetailRepository.existsByChildIdAndVaccinationDate(childId, vaccinationDate);
            if (exists) {
                errors.add("Bé " + child.getFullname()
                        + " đã có lịch tiêm vào "
                        + vaccinationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                        + ". Vui lòng chọn thời gian khác.");
                continue;
            }
        }


        for (Product product : selectedProducts) {
            int totalDosesRequired = product.getNumberOfDoses();
            int systemDosesTaken = orderDetailRepository.countDosesTaken(product.getId(), child.getId());
            int dosesRemaining = totalDosesRequired - systemDosesTaken - orderRequest.getDosesAlreadyTaken();

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

            for (int i = 0; i < dosesRemaining; i++) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderId(order.getOrderId());
                detail.setProduct(product);
                detail.setChild(child);
                detail.setQuantity(1);

                // Chỉ set ngày tiêm cho mũi đầu tiên
                if (i == 0) {
                    detail.setVaccinationDate(vaccinationDate);
                } else {
                    detail.setVaccinationDate(null);
                }

                detail.setStatus(OrderDetailStatus.CHUA_TIEM);
                detail.setFirstName(orderRequest.getFirstName());
                detail.setLastName(orderRequest.getLastName());
                detail.setEmail(orderRequest.getEmail());
                detail.setMobileNo(orderRequest.getMobileNo());

                orderDetails.add(detail);
                totalPrice += product.getPrice();
            }

            product.setReservedQuantity(product.getReservedQuantity() + dosesRemaining);
        }
    }

    // Có lỗi => không lưu gì hết
    if (!errors.isEmpty()) {
        throw new IllegalArgumentException("Không thể tạo đơn hàng vì các lỗi sau:\n" + String.join("\n", errors));
    }

    orderDetailRepository.saveAll(orderDetails);
    for (Product p : selectedProducts) {
        productRepository.save(p);
    }

    order.setTotalPrice(totalPrice);
    productOrderRepository.save(order);
    return order;
}





    @Override
    public List<ProductOrder> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

//    @Override
//    public void saveOrderByStaff(Long userId,
//                                 ProductOrder productOrder,
//                                 OrderRequest orderRequest) throws Exception {
//
//
//
//        // Lưu thông tin địa chỉ đơn hàng
//        OrderDetail orderDetail = new OrderDetail();
//        orderDetail.setFirstName(orderRequest.getFirstName());
//        orderDetail.setLastName(orderRequest.getLastName());
//        orderDetail.setEmail(orderRequest.getEmail());
//        orderDetail.setMobileNo(orderRequest.getMobileNo());
//
//        //Convert từ childId qua type user
//        User child=userRepository.findByIdDirect(orderRequest.getChildId());
//        orderDetail.setChild(child);
//
//        // Tạo đơn hàng từ giỏ hàng
//        ProductOrder order = new ProductOrder();
//        order.setOrderId(UUID.randomUUID().toString());
//        order.setOrderDate(LocalDate.now());
//       // order.setProduct(productOrder.getProduct());
//        //order.setPrice(productOrder.getProduct().getDiscountPrice());
//       // order.setQuantity(productOrder.getQuantity());
//        order.setUser(productOrder.getUser());
//        order.setStatus(productOrder.getStatus());
//        order.setPaymentType(orderRequest.getPaymentType());
//
//     //   order.setOrderDetail(orderDetail);
//
//        // Lưu đơn hàng vào database
//        ProductOrder savedOrder = orderRepository.save(order);
//
//        // Gửi email xác nhận đơn hàng
//        commonUtil.sendMailForProductOrder(savedOrder, "success");
//
//
//    }

    @Override
    @Transactional
    public ProductOrder createOrderByProductIdByStaff(Long userId,
                                                      List<Long> productId,
                                                      OrderRequest orderRequest) {

        List<Product> selectedProducts = productRepository.findAllById(productId);
        User parent = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Gom lỗi tại đây
        List<String> errors = new ArrayList<>();
        List<OrderDetail> orderDetails = new ArrayList<>();
        double totalPrice = 0.0;

        // Kiểm tra ngày tiêm mong muốn
        LocalDateTime vaccinationDate = orderRequest.getVaccinationdate();
        if (vaccinationDate != null) {
            if (vaccinationDate.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Ngày tiêm mong muốn phải từ hôm nay trở đi.");
            }

            LocalTime time = vaccinationDate.toLocalTime();
            LocalTime start = LocalTime.of(7, 30);
            LocalTime end = LocalTime.of(17, 0);
            if (time.isBefore(start) || time.isAfter(end)) {
                throw new IllegalArgumentException("Giờ tiêm chỉ được chọn từ 07:30 đến 17:00.");
            }
        }


        ProductOrder order = new ProductOrder();
        order.setOrderId("ORD" + System.currentTimeMillis());
        order.setOrderDate(LocalDate.now());
        order.setStatus(OrderStatus.ORDER_RECEIVED.getName());
        order.setPaymentType("VNPAY");
        order.setUser(parent);
        productOrderRepository.save(order); // vẫn tạo trước

        for (Long childId : orderRequest.getChildId()) {
            User child = userRepository.findByIdDirect(childId);
            if (child == null || !Objects.equals(child.getParentid(), userId)) {
                errors.add("Trẻ ID " + childId + " không hợp lệ hoặc không thuộc phụ huynh.");
                continue;
            }

            if (vaccinationDate != null) {
                boolean exists = orderDetailRepository.existsByChildIdAndVaccinationDate(childId, vaccinationDate);
                if (exists) {
                    errors.add("Bé " + child.getFullname()
                            + " đã có lịch tiêm vào "
                            + vaccinationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                            + ". Vui lòng chọn thời gian khác.");
                    continue;
                }
            }


            for (Product product : selectedProducts) {
                int totalDosesRequired = product.getNumberOfDoses();
                int systemDosesTaken = orderDetailRepository.countDosesTaken(product.getId(), child.getId());
                int dosesRemaining = totalDosesRequired - systemDosesTaken - orderRequest.getDosesAlreadyTaken();

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

                for (int i = 0; i < dosesRemaining; i++) {
                    OrderDetail detail = new OrderDetail();
                    detail.setOrderId(order.getOrderId());
                    detail.setProduct(product);
                    detail.setChild(child);
                    detail.setQuantity(1);

                    // Chỉ set ngày tiêm cho mũi đầu tiên
                    if (i == 0) {
                        detail.setVaccinationDate(vaccinationDate);
                    } else {
                        detail.setVaccinationDate(null);
                    }

                    detail.setVaccinationDate(orderRequest.getVaccinationdate());
                    detail.setStatus(OrderDetailStatus.CHUA_TIEM);
                    detail.setFirstName(orderRequest.getFirstName());
                    detail.setLastName(orderRequest.getLastName());
                    detail.setEmail(orderRequest.getEmail());
                    detail.setMobileNo(orderRequest.getMobileNo());

                    orderDetails.add(detail);
                    totalPrice += product.getPrice();
                }

                product.setReservedQuantity(product.getReservedQuantity() + dosesRemaining);
            }
        }

        //Nếu có lỗi => hủy đơn + trả lỗi
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Không thể tạo đơn hàng vì các lỗi sau:\n" + String.join("\n", errors));
        }

        // Lưu dữ liệu nếu không có lỗi
        orderDetailRepository.saveAll(orderDetails);
        for (Product p : selectedProducts) {
            productRepository.save(p);
        }

        order.setTotalPrice(totalPrice);
        productOrderRepository.save(order);
        return order;
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

    @Override
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
    @Transactional
    public void updateOrderDetailStatus(Long orderDetailId, OrderDetailStatus status) {
        OrderDetail detail = orderDetailRepository.findById(Math.toIntExact(orderDetailId))
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy OrderDetail với ID: " + orderDetailId));

        detail.setStatus(status);
        orderDetailRepository.save(detail);

        // Kiểm tra nếu tất cả OrderDetail cùng orderId đều đã tiêm => cập nhật ProductOrder thành SUCCESS
        String orderId = detail.getOrderId();
        List<OrderDetail> allDetails = orderDetailRepository.findByOrderId(orderId);

        boolean allDone = allDetails.stream()
                .allMatch(d -> d.getStatus() == OrderDetailStatus.DA_TIEM);

        ProductOrder order = productOrderRepository.findByOrderId(orderId);

        if (order != null) {  // Kiểm tra order có tồn tại không
            order.setStatus(OrderStatus.SUCCESS.getName());
            productOrderRepository.save(order);
        } else {
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
                od.getStatus().name()
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
                od.getStatus().name()
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
                od.getStatus().name()
        )).collect(Collectors.toList());
    }

//    @Override
//    public List<Product> suggestVaccinesForChild(Long childId) {
//        // Lấy user đang login
//        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long userId = jwt.getClaim("id");
//
//        User child = userRepository.findByIdDirect(childId);
//        if (child == null || !Objects.equals(child.getParentid(), userId)) {
//            throw new IllegalArgumentException("Đây không phải là trẻ của bạn");
//        }
//
//        int ageInMonths = Period.between(child.getBod(), LocalDate.now()).getYears() * 12
//                + Period.between(child.getBod(), LocalDate.now()).getMonths();
//        List<Product> all = productRepository.findAll();
//        all.forEach(p -> {
//            System.out.println("Product: " + p.getTitle() +
//                    ", minAge: " + p.getMinAgeMonths() +
//                    ", maxAge: " + p.getMaxAgeMonths());
//        });
//
//
//        List<Product> suitable = productRepository.findSuitableProductsForAge(ageInMonths);
//
//        return suitable.stream()
//                .filter(product -> {
//                    int taken = orderDetailRepository.countDosesTaken(product.getId(), childId);
//                    int remaining = product.getNumberOfDoses() - taken;
//                    return remaining > 0;
//                })
//                .toList();
//    }
@Override
public List<Product> suggestVaccinesForChild(Long childId) {
    // Lấy user đang login
    Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = jwt.getClaim("id");

    // Kiểm tra quyền truy cập
    User child = userRepository.findByIdDirect(childId);
    if (child == null || !Objects.equals(child.getParentid(), userId)) {
        throw new IllegalArgumentException("Đây không phải là trẻ của bạn");
    }

    // Tính tuổi theo tháng
    int ageInMonths = Period.between(child.getBod(), LocalDate.now()).getYears() * 12
            + Period.between(child.getBod(), LocalDate.now()).getMonths();

    // Lấy toàn bộ sản phẩm từ DB (bao gồm cả các trường minAge, maxAge, priorityTag...)
    List<Product> allProducts = productRepository.findAll();

    return allProducts.stream()
            // Kiểm tra độ tuổi phù hợp
            .filter(product -> {
                Integer minAge = product.getMinAgeMonths();
                Integer maxAge = product.getMaxAgeMonths();
                boolean ageValid = (minAge == null || ageInMonths >= minAge)
                        && (maxAge == null || ageInMonths <= maxAge);
                return ageValid;
            })
            // Kiểm tra số mũi còn lại
            .filter(product -> {
                int taken = orderDetailRepository.countDosesTaken(product.getId(), childId);
                Integer numberOfDoses = product.getNumberOfDoses();
                return numberOfDoses != null && taken < numberOfDoses;
            })
            // Kiểm tra còn hàng
            .filter(product -> {
                Integer quantity = product.getQuantity();
                Integer reserved = product.getReservedQuantity();
                return quantity != null && reserved != null && (quantity - reserved) > 0;
            })
            // Sắp xếp theo tag ưu tiên (true lên trước)
            .sorted(Comparator.comparing(Product::getIsPriority, Comparator.nullsLast(Comparator.reverseOrder())))
            .toList();
}


    @Override
    public List<Product> suggestVaccinesByStaff(Long childId) {

        User child = userRepository.findByIdDirect(childId);

        int ageInMonths = Period.between(child.getBod(), LocalDate.now()).getYears() * 12
                + Period.between(child.getBod(), LocalDate.now()).getMonths();
        List<Product> all = productRepository.findAll();
        all.forEach(p -> {
            System.out.println("Product: " + p.getTitle() +
                    ", minAge: " + p.getMinAgeMonths() +
                    ", maxAge: " + p.getMaxAgeMonths());
        });


        List<Product> suitable = productRepository.findSuitableProductsForAge(ageInMonths);

        return suitable.stream()
                .filter(product -> {
                    int taken = orderDetailRepository.countDosesTaken(product.getId(), childId);
                    int remaining = product.getNumberOfDoses() - taken;
                    return remaining > 0;
                })
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
                    detail.setVaccinationDate(null);

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
                od.getStatus().name()
        )).toList();
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

