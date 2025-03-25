package com.example.SpringBootTurialVip.service.serviceimpl;

import com.example.SpringBootTurialVip.dto.response.CategoryTreeResponse;
import com.example.SpringBootTurialVip.enums.CategoryType;
import com.example.SpringBootTurialVip.service.CategoryService;
import com.example.SpringBootTurialVip.entity.Category;
import com.example.SpringBootTurialVip.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Boolean existCategory(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    public Boolean deleteCategory(int id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (!ObjectUtils.isEmpty(category)) {
            if (categoryRepository.existsByParent(category)) {
                throw new RuntimeException("Không thể xoá vì có danh mục con");
            }
            categoryRepository.delete(category);
            return true;
        }
        return false;
    }

    @Override
    public Category getCategoryById(int id) {
        Category category = categoryRepository.findById(id).orElse(null);
        return category;
    }

    @Override
    public List<Category> getAllActiveCategory() {
        List<Category> categories = categoryRepository.findByIsActiveTrue();
        return categories;
    }

    @Override
    public Page<Category> getAllCategorPagination(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> findByNameContaining(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
   //@Transactional(readOnly = true)

    public List<CategoryTreeResponse> getCategoryTreeByType(CategoryType type) {
        List<Category> all;

        if (type == null) {
            all = categoryRepository.findAll(); // Lấy tất cả
        } else {
            all = categoryRepository.findByType(type);
        }

        List<Category> roots = all.stream()
                .filter(c -> c.getParent() == null)
                .toList();

        return roots.stream().map(this::mapCategoryToTree).toList();
    }

    private CategoryTreeResponse mapCategoryToTree(Category category) {
        List<CategoryTreeResponse> subTrees = category.getSubCategories().stream()
                .map(this::mapCategoryToTree)
                .toList();
        System.out.println("🔍 Mapping: " + category.getName() + " | sub = " + category.getSubCategories().size());

        return new CategoryTreeResponse(
                category.getId(),
                category.getName(),
                category.getType(),
                category.getImageName(),
                category.getIsActive(),
                subTrees
        );

    }

}
