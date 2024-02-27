package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.category.CategoryDto;
import com.pagepal.capstone.entities.postgre.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto toDto(Category category);
}
