package com.pagepal.capstone.dtos.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WriteBookDto {
    private String title;
    private String longTitle;
    private String author;
    private String publisher;
    private Long pages;
    private String language;
    private String overview;
    private String imageUrl;
    private String edition;
    private String createdAt;
    private UUID categoryId;
}
