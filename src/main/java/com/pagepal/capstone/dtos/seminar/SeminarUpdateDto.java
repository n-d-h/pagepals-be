package com.pagepal.capstone.dtos.seminar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeminarUpdateDto {
    private UUID id;
    private String title;
    private String description;
    private String imageUrl;
    private Integer duration;
}