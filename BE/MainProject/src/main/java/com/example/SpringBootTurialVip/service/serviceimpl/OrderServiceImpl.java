package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.OrderRequest;
import com.example.SpringBootTurialVip.entity.*;
import com.example.SpringBootTurialVip.repository.*;
import com.example.SpringBootTurialVip.service.NotificationService;
import com.example.SpringBootTurialVip.service.OrderService;
import com.example.SpringBootTurialVip.util.CommonUtil;
import com.example.SpringBootTurialVip.enums.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
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



    @Override
    public void saveOrder(Long cartId, OrderRequest orderRequest) throws Exception {
        // Tìm giỏ hàng theo cartId
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NoSuchElementException("Cart với ID " + cartId + " không tồn tại"));



        // Lưu thông tin địa chỉ đơn hàng
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setFirstName(orderRequest.getFirstName());
        orderDetail.setLastName(orderRequest.getLastName());
        orderDetail.setEmail(orderRequest.getEmail());
        orderDetail.setMobileNo(orderRequest.getMobileNo());

        //Convert từ childId qua type user
        User child=userRepository.findByIdDirect(orderRequest.getChildId());
        orderDetail.setChild(child);

        // Tạo đơn hàng từ giỏ hàng
        ProductOrder order = new ProductOrder();
        order.setOrderId("ORD" + System.currentTimeMillis());
        order.setOrderDate(LocalDate.now());
        order.setProduct(cart.getProduct());
        order.setPrice(cart.getProduct().getDiscountPrice());
        order.setQuantity(cart.getQuantity());
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.IN_PROGRESS.getName());
        order.setPaymentType(orderRequest.getPaymentType());

        order.setOrderDetail(orderDetail);

        // Lưu đơn hàng vào database
        ProductOrder savedOrder = orderRepository.save(order);

        // Gửi email xác nhận đơn hàng
        commonUtil.sendMailForProductOrder(savedOrder, "success");

        // Xóa sản phẩm khỏi giỏ hàng sau khi đặt hàng thành công
        cartRepository.delete(cart);
    }


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

    @Override
    public List<VaccineOrderStats> getLeastOrderedVaccines(int month, int year) {
        return productOrderRepository.findLeastOrderedVaccines(month, year);
    }

    @Override
    public ProductOrder getOrderById(Long orderId) {
        return productOrderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found with ID: " + orderId));
    }

    @Override
    @Transactional//Nếu 1 bc trong db bị lỗi sẽ rollback tránh thừa thiếu dữ liệu ko xác định
    public ProductOrder createOrderByProductId(Long productId, int quantity, OrderRequest orderRequest) {
        // Lấy thông tin sản phẩm
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // Lấy thông tin user từ JWT
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Tạo thông tin chi tiết đơn hàng
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setEmail(orderRequest.getEmail());
        orderDetail.setFirstName(orderRequest.getFirstName());
        orderDetail.setLastName(orderRequest.getLastName());
        orderDetail.setMobileNo(orderRequest.getMobileNo());
        //Convert từ childId qua type user
        User child=userRepository.findByIdDirect(orderRequest.getChildId());
        orderDetail.setChild(child);


        // Tạo đơn hàng mới
        ProductOrder order = new ProductOrder();
        order.setOrderId("ORD" + System.currentTimeMillis()); // Tạo mã đơn hàng
        order.setOrderDate(LocalDate.now());
        order.setProduct(product);
        order.setPrice(product.getPrice() * quantity);
        order.setQuantity(quantity);
        order.setStatus(OrderStatus.ORDER_RECEIVED.getName()); // Trạng thái mặc định là "Order Received"
        order.setPaymentType(orderRequest.getPaymentType());
        order.setUser(user);
        order.setOrderDetail(orderDetail); // Gán thông tin chi tiết đơn hàng

        return productOrderRepository.save(order);
    }

    @Override
    public List<ProductOrder> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public void saveOrderByStaff(Long userId,
                                 ProductOrder productOrder,
                                 OrderRequest orderRequest) throws Exception {



        // Lưu thông tin địa chỉ đơn hàng
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setFirstName(orderRequest.getFirstName());
        orderDetail.setLastName(orderRequest.getLastName());
        orderDetail.setEmail(orderRequest.getEmail());
        orderDetail.setMobileNo(orderRequest.getMobileNo());

        //Convert từ childId qua type user
        User child=userRepository.findByIdDirect(orderRequest.getChildId());
        orderDetail.setChild(child);

        // Tạo đơn hàng từ giỏ hàng
        ProductOrder order = new ProductOrder();
        order.setOrderId(UUID.randomUUID().toString());
        order.setOrderDate(LocalDate.now());
        order.setProduct(productOrder.getProduct());
        order.setPrice(productOrder.getProduct().getDiscountPrice());
        order.setQuantity(productOrder.getQuantity());
        order.setUser(productOrder.getUser());
        order.setStatus(productOrder.getStatus());
        order.setPaymentType(orderRequest.getPaymentType());

        order.setOrderDetail(orderDetail);

        // Lưu đơn hàng vào database
        ProductOrder savedOrder = orderRepository.save(order);

        // Gửi email xác nhận đơn hàng
        commonUtil.sendMailForProductOrder(savedOrder, "success");


    }


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
