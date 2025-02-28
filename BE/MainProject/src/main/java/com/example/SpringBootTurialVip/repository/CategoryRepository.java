package com.example.SpringBootTurialVip.repository;


import com.example.SpringBootTurialVip.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	public Boolean existsByName(String name);

	public List<Category> findByIsActiveTrue();

	List<Category> findByNameContainingIgnoreCase(String name);

}
