package com.example.SpringBootTurialVip.repository;


import com.example.SpringBootTurialVip.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	public Boolean existsByName(String name);

	public List<Category> findByIsActiveTrue();

	List<Category> findByNameContainingIgnoreCase(String name);

	Optional<Category> findById(Long id);

	Optional<Category> findByName(String name);
}
