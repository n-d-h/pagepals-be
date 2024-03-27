package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.category.CategoryDto;
import com.pagepal.capstone.entities.postgre.Category;
import com.pagepal.capstone.mappers.CategoryMapper;
import com.pagepal.capstone.repositories.CategoryRepository;
import com.pagepal.capstone.services.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public List<CategoryDto> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryMapper.INSTANCE::toDto).toList();
    }

    @Override
    public CategoryDto getCategoryById(UUID id) {
        return null;
    }
}
