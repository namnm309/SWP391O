package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.service.OrderService;
import com.example.SpringBootTurialVip.shopentity.Cart;
import com.example.SpringBootTurialVip.shopentity.OrderDetail;
import com.example.SpringBootTurialVip.shopentity.OrderRequest;
import com.example.SpringBootTurialVip.shopentity.ProductOrder;
import com.example.SpringBootTurialVip.shoprepository.CartRepository;
import com.example.SpringBootTurialVip.shoprepository.ProductOrderRepository;
import com.example.SpringBootTurialVip.util.CommonUtil;
import com.example.SpringBootTurialVip.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

        // Tạo đơn hàng từ giỏ hàng
        ProductOrder order = new ProductOrder();
        order.setOrderId(UUID.randomUUID().toString());
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
            return updateOrder;
        }
        return null;
    }

    @Override
    public List<ProductOrder> getAllOrders() {
        return List.of();
    }

    @Override
    public ProductOrder getOrdersByOrderId(String orderId) {
        return null;
    }

    @Override
    public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize) {
        return null;
    }
}
