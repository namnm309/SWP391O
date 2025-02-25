package com.example.SpringBootTurialVip.shoprepository;


import com.example.SpringBootTurialVip.shopentity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	public Boolean existsByName(String name);

	public List<Category> findByIsActiveTrue();

	List<Category> findByNameContainingIgnoreCase(String name);

}
