package com.pagepal.capstone.dtos.category;

import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private String id;
    private String name;
    private String description;
}
