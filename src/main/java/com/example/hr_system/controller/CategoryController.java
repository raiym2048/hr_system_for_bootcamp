package com.example.hr_system.controller;

import com.example.hr_system.dto.category.CategoryRequest;
import com.example.hr_system.dto.category.CategoryResponse;
import com.example.hr_system.entities.Category;
import com.example.hr_system.mapper.CategoryMapper;
import com.example.hr_system.repository.CategoryRepository;
import com.example.hr_system.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoryController {

    private final CategoryServiceImpl categoryService;

    @GetMapping("/categories")
    public List<CategoryResponse> getAllCategories1() {
        return categoryService.getAllCategory();
    }

    @GetMapping("/get/category/{id}")
    public CategoryResponse getCategoryById(@PathVariable("id") Long id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("delete/category/{id}")
    public void deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategoryById(id);
    }

    @PostMapping("/update/category/{id}")
    public CategoryResponse updateCategory(@PathVariable("id") Long id, @RequestBody CategoryRequest categoryRequest) {
        return categoryService.updateCategory(id, categoryRequest);
    }


    @PostMapping("/create/category")
    public void createCategory(@RequestBody CategoryRequest categoryRequest) {
        categoryService.createCategory(categoryRequest);
    }


}
