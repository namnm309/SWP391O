package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.shopentity.OrderRequest;
import com.example.SpringBootTurialVip.shopentity.ProductOrder;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    public void saveOrder(Long cartid, OrderRequest orderRequest) throws Exception;

    public List<ProductOrder> getOrdersByUser(Integer userId);

    public ProductOrder updateOrderStatus(Integer id, String status);

    public List<ProductOrder> getAllOrders();

    public ProductOrder getOrdersByOrderId(String orderId);

    public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize);
}
