package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.request.OrderRequest;
import com.example.SpringBootTurialVip.dto.response.UpcomingVaccinationResponse;
import com.example.SpringBootTurialVip.dto.response.VaccinationHistoryResponse;
import com.example.SpringBootTurialVip.entity.*;
import com.example.SpringBootTurialVip.enums.OrderDetailStatus;
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
import java.util.*;

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

    @Autowired
    private OrderDetailRepository orderDetailRepository;



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
       // order.setProduct(cart.getProduct());
       // order.setPrice(cart.getProduct().getDiscountPrice());
       // order.setQuantity(cart.getQuantity());
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.IN_PROGRESS.getName());
        order.setPaymentType(orderRequest.getPaymentType());

        //order.setOrderDetail(orderDetail);

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

    @Override
    @Transactional//Nếu 1 bc trong db bị lỗi sẽ rollback tránh thừa thiếu dữ liệu ko xác định
    public ProductOrder createOrderByProductId(List<Long> productId,
                                               List<Integer> quantity,
                                               OrderRequest orderRequest) {
        // Lấy thông tin sản phẩm
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
        // Lấy danh sách sản phẩm theo danh sách ID
        List<Product> selectedProducts = productRepository.findAllById(productId);

        // Lấy thông tin user từ JWT
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = jwt.getClaim("id");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Tạo đơn hàng mới
        ProductOrder order = new ProductOrder();
        order.setOrderId("ORD" + System.currentTimeMillis()); // Tạo mã đơn hàng
        order.setOrderDate(LocalDate.now());
//        order.setProduct(product);
//        order.setPrice(product.getPrice() * quantity);
//        order.setQuantity(quantity);
        order.setStatus(OrderStatus.ORDER_RECEIVED.getName()); // Trạng thái mặc định là "Order Received"
        order.setPaymentType("VNPAY");
        order.setUser(user);
//        order.setOrderDetail(orderDetail); // Gán thông tin chi tiết đơn hàng
        productOrderRepository.save(order);

        double totalPrice = 0.0;
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (int i = 0; i < selectedProducts.size(); i++) {
            Product product = selectedProducts.get(i);
            int quantiti = quantity.get(i); // Số lượng mũi tiêm

            for (int dose = 1; dose <= quantiti; dose++) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(order.getOrderId()); // Gán orderId từ ProductOrder
                orderDetail.setEmail(orderRequest.getEmail());
                orderDetail.setFirstName(orderRequest.getFirstName());
                orderDetail.setLastName(orderRequest.getLastName());
                orderDetail.setMobileNo(orderRequest.getMobileNo());

                //Bắt lỗi nếu trẻ không tồn tại
                User child=userRepository.findByIdDirect(orderRequest.getChildId());
                if (child == null) {
                    throw new IllegalArgumentException("Trẻ có ID " + orderRequest.getChildId() + " không tồn tại.");
                } else if (child.getParentid() != userId) {
                    throw new IllegalArgumentException("Trẻ có ID "+orderRequest.getChildId()+" không phải là trẻ của bạn");
                }
                orderDetail.setChild(child);

                orderDetail.setProduct(product);
                orderDetail.setQuantity(1); // Mỗi OrderDetail chỉ lưu 1 mũi tiêm
                orderDetail.setVaccinationDate(null); // Staff sẽ cập nhật sau

                // **Lấy đúng giá trị sản phẩm**
                double orderDetailPrice = product.getPrice(); // **Lấy giá đúng từ Product**                l
                totalPrice += orderDetailPrice; // **Cộng giá vào tổng giá đơn hàng**

                orderDetails.add(orderDetail);
            }
        }
        // Lưu danh sách OrderDetail sau khi có ProductOrder**
        orderDetailRepository.saveAll(orderDetails);

        // Cập nhật tổng giá trị đơn hàng**
        order.setTotalPrice(totalPrice);
        productOrderRepository.save(order); // Cập nhật totalPrice

        return order;
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
       // order.setProduct(productOrder.getProduct());
        //order.setPrice(productOrder.getProduct().getDiscountPrice());
       // order.setQuantity(productOrder.getQuantity());
        order.setUser(productOrder.getUser());
        order.setStatus(productOrder.getStatus());
        order.setPaymentType(orderRequest.getPaymentType());

     //   order.setOrderDetail(orderDetail);

        // Lưu đơn hàng vào database
        ProductOrder savedOrder = orderRepository.save(order);

        // Gửi email xác nhận đơn hàng
        commonUtil.sendMailForProductOrder(savedOrder, "success");


    }

    @Override
    @Transactional//Nếu 1 bc trong db bị lỗi sẽ rollback tránh thừa thiếu dữ liệu ko xác định
    public ProductOrder createOrderByProductIdByStaff(Long userId,
                                                      List<Long> productId,
                                               List<Integer> quantity,
                                               OrderRequest orderRequest) {
        // Lấy thông tin sản phẩm
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
        // Lấy danh sách sản phẩm theo danh sách ID
        List<Product> selectedProducts = productRepository.findAllById(productId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Tạo đơn hàng mới
        ProductOrder order = new ProductOrder();
        order.setOrderId("ORD" + System.currentTimeMillis()); // Tạo mã đơn hàng
        order.setOrderDate(LocalDate.now());
//        order.setProduct(product);
//        order.setPrice(product.getPrice() * quantity);
//        order.setQuantity(quantity);
        order.setStatus(OrderStatus.ORDER_RECEIVED.getName()); // Trạng thái mặc định là "Order Received"
        order.setPaymentType("VNPAY");
        order.setUser(user);
//        order.setOrderDetail(orderDetail); // Gán thông tin chi tiết đơn hàng
        productOrderRepository.save(order);

        double totalPrice = 0.0;
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (int i = 0; i < selectedProducts.size(); i++) {
            Product product = selectedProducts.get(i);
            int quantiti = quantity.get(i); // Số lượng mũi tiêm

            for (int dose = 1; dose <= quantiti; dose++) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(order.getOrderId()); // Gán orderId từ ProductOrder
                orderDetail.setEmail(orderRequest.getEmail());
                orderDetail.setFirstName(orderRequest.getFirstName());
                orderDetail.setLastName(orderRequest.getLastName());
                orderDetail.setMobileNo(orderRequest.getMobileNo());
                User child=userRepository.findByIdDirect(orderRequest.getChildId());
                orderDetail.setChild(child);
                orderDetail.setProduct(product);
                orderDetail.setQuantity(1); // Mỗi OrderDetail chỉ lưu 1 mũi tiêm
                orderDetail.setVaccinationDate(null); // Staff sẽ cập nhật sau

                // **Lấy đúng giá trị sản phẩm**
                double orderDetailPrice = product.getPrice(); // **Lấy giá đúng từ Product**                l
                totalPrice += orderDetailPrice; // **Cộng giá vào tổng giá đơn hàng**

                orderDetails.add(orderDetail);
            }
        }
        // Lưu danh sách OrderDetail sau khi có ProductOrder**
        orderDetailRepository.saveAll(orderDetails);

        // Cập nhật tổng giá trị đơn hàng**
        order.setTotalPrice(totalPrice);
        productOrderRepository.save(order); // Cập nhật totalPrice

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
    @Transactional
    public void updateOrderDetailStatus(Long orderDetailId, OrderDetailStatus newStatus) {
        OrderDetail orderDetail = orderDetailRepository.findById(Math.toIntExact(orderDetailId))
                .orElseThrow(() -> new RuntimeException("OrderDetail not found"));

        // Cập nhật trạng thái của OrderDetail
        orderDetail.setStatus(newStatus);
        orderDetailRepository.save(orderDetail);

        // Lấy order_id từ orderDetail
        String orderId = orderDetail.getOrderId(); // Giả sử OrderDetail có trường orderId

        // Kiểm tra nếu tất cả OrderDetail của order_id đã có trạng thái "Đã tiêm chủng"
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        boolean allVaccinated = orderDetails.stream()
                .allMatch(detail -> detail.getStatus() == OrderDetailStatus.DA_TIEM);

        if (allVaccinated) {
            // Cập nhật trạng thái ProductOrder thành SUCCESS
            ProductOrder productOrder = productOrderRepository.findByOrderId(orderId);
            if (productOrder == null) {
                throw new RuntimeException("ProductOrder not found");
            }

            productOrder.setStatus(String.valueOf(OrderStatus.SUCCESS));
            productOrderRepository.save(productOrder);
        }
    }

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
