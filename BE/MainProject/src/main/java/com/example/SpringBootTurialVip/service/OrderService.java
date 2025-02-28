package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.entity.OrderRequest;
import com.example.SpringBootTurialVip.entity.ProductOrder;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    public void saveOrder(Long cartid, OrderRequest orderRequest) throws Exception;

    public List<ProductOrder> getOrdersByUser(Long userId);

    public ProductOrder updateOrderStatus(Long id, String status);

    //Admin dashboard : Số đơn vaccine trung bình 1 ngày
    //public double getAverageOrdersPerDay() ;

    public List<ProductOrder> getAllOrders();

    public ProductOrder getOrdersByOrderId(String orderId);

    public Page<ProductOrder> getAllOrdersPagination(Integer pageNo, Integer pageSize);
}
