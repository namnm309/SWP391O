package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.shopentity.Cart;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CartService {

    public Cart saveCart(Long productId, Long userId);

    public List<Cart> getCartsByUser(Long userId);

    public Long getCountCart(Long userId);

    public void updateQuantity(String sy, Long cid);
}
