package com.example.SpringBootTurialVip.service;

import com.example.SpringBootTurialVip.entity.Cart;

import java.util.List;


public interface CartService {

    public Cart saveCart(Long productId, Long userId);

    public List<Cart> getCartsByUser(Long userId);

    public Cart getCartById(Long cid);

    public Long getCountCart(Long userId);

    public void updateQuantity(String sy, Long cid);
}
