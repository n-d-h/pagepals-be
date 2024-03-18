package com.pagepal.capstone.dtos.book;

import com.pagepal.capstone.entities.postgre.Author;
import com.pagepal.capstone.entities.postgre.Category;
import com.pagepal.capstone.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private UUID id;

    private String externalId;

    private String title;

    private String publisher;

    private String publishedDate;

    private String description;

    private Integer pageCount;

    private String smallThumbnailUrl;

    private String thumbnailUrl;

    private String language;

    private Status status;

    private List<Category> categories;

    private List<Author> authors;

}
