package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.category.CategoryDto;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<CategoryDto> getCategories();

    CategoryDto getCategoryById(UUID id);

//    CategoryDto createCategory(CategoryCreateDto categoryCreateDto);

//    CategoryDto updateCategory(UUID id, CategoryUpdateDto categoryUpdateDto);

//    void deleteCategory(UUID id);
}
