package com.example.SpringBootTurialVip.repository;


import com.example.SpringBootTurialVip.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByIsActiveTrue();

	Page<Product> findByIsActiveTrue(Pageable pageable);

	@Query("SELECT p FROM Product p WHERE p.category.name = :categoryName")
	List<Product> findByCategory(String category);

	//List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2);

	Page<Product> findByCategory(Pageable pageable, String category);

	Page<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2,
			Pageable pageable);

	Page<Product> findByisActiveTrueAndTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch, String ch2,
			Pageable pageable);

	//List<Product> findByTitle(String title);

	@Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))")
	List<Product> findByTitle(@Param("title") String title);

	boolean existsByTitle(String title); // Kiểm tra trùng tên sản phẩm

	List<Product> findAllByIsActiveTrue(); // Lấy danh sách sản phẩm chưa bị ẩn

	// Tìm danh sách sản phẩm theo ID danh mục
	List<Product> findByCategoryId(Long categoryId);

	// Tìm danh sách sản phẩm theo tên danh mục (nếu cần)
	List<Product> findByCategory_Name(String categoryName);


}
