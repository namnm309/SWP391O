package com.example.SpringBootTurialVip.repository;


import com.example.SpringBootTurialVip.entity.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

	public Cart findByProductIdAndUserId(Long productId, Long userId);

	public Long countByUserId(Long userId);

	public List<Cart> findByUserId(Long userId);

	// Xóa tất cả sản phẩm trong giỏ hàng theo userId
	@Transactional
	@Modifying
	@Query("DELETE FROM Cart c WHERE c.user.id = :userId")
	public void deleteByUserId(@Param("userId") Long userId);

}
