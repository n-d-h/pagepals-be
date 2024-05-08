package com.pagepal.capstone.dtos.seminar;

import com.pagepal.capstone.dtos.googlebook.GoogleBook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeminarCreateDto {
    private UUID readerId;
    private GoogleBook book;
    private String title;
    private String description;
    private String imageUrl;
    private Integer duration;
}
