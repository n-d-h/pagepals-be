package com.pagepal.capstone.dtos.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WriteServiceDto {
    private Double price;
    private String description;
    private Double duration;
    private UUID readerId;
    private UUID chapterId;
}
