package com.example.hr_system.mapper;

import com.example.hr_system.dto.category.CategoryRequest;
import com.example.hr_system.dto.category.CategoryResponse;
import com.example.hr_system.entities.Category;

import java.util.List;

public interface CategoryMapper {

    CategoryResponse toDto(Category category);

    List<CategoryResponse> toDtos(List<Category> categories);

    CategoryResponse requestToResponse(CategoryRequest categoryRequest);
}
