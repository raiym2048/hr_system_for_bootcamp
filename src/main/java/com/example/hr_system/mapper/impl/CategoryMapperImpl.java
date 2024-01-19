package com.example.hr_system.mapper.impl;

import com.example.hr_system.dto.category.CategoryRequest;
import com.example.hr_system.dto.category.CategoryResponse;
import com.example.hr_system.entities.Category;
import com.example.hr_system.mapper.CategoryMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapperImpl implements CategoryMapper {
    @Override
    public CategoryResponse toDto(Category category) {
        if (category == null) {
            return null;
        }
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        return response;
    }

    @Override
    public List<CategoryResponse> toDtos(List<Category> categories) {
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category category : categories) {
            categoryResponses.add(toDto(category));
        }
        return categoryResponses;
    }

    @Override
    public CategoryResponse requestToResponse(CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setName(categoryRequest.getName());
        return null;
    }
}
