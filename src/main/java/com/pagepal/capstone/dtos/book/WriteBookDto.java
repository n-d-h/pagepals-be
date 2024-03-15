package com.pagepal.capstone.dtos.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
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
