package com.example.SpringBootTurialVip.dto.response;

import com.example.SpringBootTurialVip.entity.Category;
import com.example.SpringBootTurialVip.enums.CategoryType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class CategoryTreeResponse {
    private Long id;
    private String name;
    private CategoryType type;
    private String imageName;
    private Boolean isActive;
    private List<CategoryTreeResponse> subCategories;

    public CategoryTreeResponse(Long id,
                                String name,
                                CategoryType type,
                                String imageName,
                                Boolean isActive,
                                List<CategoryTreeResponse> subCategories) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.imageName = imageName;
        this.isActive = isActive;
        this.subCategories = subCategories;
    }



}
