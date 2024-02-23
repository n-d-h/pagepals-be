package com.pagepal.capstone.dtos.book;

import com.pagepal.capstone.dtos.category.CategoryDto;
import com.pagepal.capstone.entities.postgre.Chapter;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private UUID id;
    private String title;
    private String longTitle;
    private String author;
    private String publisher;
    private Long pages;
    private String language;
    private String overview;
    private String imageUrl;
    private String edition;
    private Date createdAt;
    private CategoryDto category;
    private List<Chapter> chapters;
}
