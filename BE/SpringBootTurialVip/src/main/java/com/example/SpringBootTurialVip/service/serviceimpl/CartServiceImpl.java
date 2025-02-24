package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.entity.User;
import com.example.SpringBootTurialVip.repository.UserRepository;
import com.example.SpringBootTurialVip.service.CartService;
import com.example.SpringBootTurialVip.shopentity.Cart;
import com.example.SpringBootTurialVip.shopentity.Category;
import com.example.SpringBootTurialVip.shopentity.Product;
import com.example.SpringBootTurialVip.shoprepository.CartRepository;
import com.example.SpringBootTurialVip.shoprepository.CategoryRepository;
import com.example.SpringBootTurialVip.shoprepository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;



    @Override
    public Cart saveCart(Long productId, Long userId) {
        User user = userRepository.findById(userId).get();
        Product product = productRepository.findById(productId).get();

        Cart cartStatus = cartRepository.findByProductIdAndUserId(productId, userId);

        Cart cart = null;

        if (ObjectUtils.isEmpty(cartStatus)) {
            cart = new Cart();
            cart.setProduct(product);
            cart.setUser(user);
            cart.setQuantity(1);
            cart.setTotalPrice(1 * product.getDiscountPrice());
        } else {
            cart = cartStatus;
            cart.setQuantity(cart.getQuantity() + 1);
            cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getDiscountPrice());
        }
        Cart saveCart = cartRepository.save(cart);

        return saveCart;
    }

    @Override
    public List<Cart> getCartsByUser(Long userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);

        Double totalOrderPrice = 0.0;
        List<Cart> updateCarts = new ArrayList<>();
        for (Cart c : carts) {
            Double totalPrice = (c.getProduct().getDiscountPrice() * c.getQuantity());
            c.setTotalPrice(totalPrice);
            totalOrderPrice = totalOrderPrice + totalPrice;
            c.setTotalOrderPrice(totalOrderPrice);
            updateCarts.add(c);
        }

        return updateCarts;
    }

    @Override
    public Long getCountCart(Long userId) {
        Long countByUserId = cartRepository.countByUserId(userId);
        return countByUserId;
    }



    @Override
    public void updateQuantity(String sy, Long cid) {
        Cart cart = cartRepository.findById(cid).get();
        int updateQuantity;

        if (sy.equalsIgnoreCase("de")) {
            updateQuantity = cart.getQuantity() - 1;

            if (updateQuantity <= 0) {
                cartRepository.delete(cart);
            } else {
                cart.setQuantity(updateQuantity);
                cartRepository.save(cart);
            }

        } else {
            updateQuantity = cart.getQuantity() + 1;
            cart.setQuantity(updateQuantity);
            cartRepository.save(cart);
        }

    }
    }

