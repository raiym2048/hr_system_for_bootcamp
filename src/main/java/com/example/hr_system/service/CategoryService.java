package com.example.hr_system.service;

import com.example.hr_system.dto.category.CategoryRequest;
import com.example.hr_system.dto.category.CategoryResponse;
import com.example.hr_system.entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<CategoryResponse> getAllCategory();

    CategoryResponse getCategoryById(Long id);

    CategoryResponse createCategory(CategoryRequest categoryRequest);

    void deleteCategoryById(Long id);

    CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest);

    void getAllUsersMessages(String token, String email);
}
