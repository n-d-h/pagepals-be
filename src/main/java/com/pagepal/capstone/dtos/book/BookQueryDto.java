package com.pagepal.capstone.dtos.book;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookQueryDto {
    @NotNull(message = "Search is required")
    private String search;
    private String sort;
    private String author;
    private UUID categoryId;
    private Integer page;
    private Integer pageSize;
}
