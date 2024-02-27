package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.category.CategoryDto;
import com.pagepal.capstone.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @QueryMapping
    public List<CategoryDto> getListCategories() {
        return categoryService.getCategories();
    }
}
