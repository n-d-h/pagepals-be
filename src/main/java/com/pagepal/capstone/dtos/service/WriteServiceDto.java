package com.pagepal.capstone.dtos.service;

import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WriteServiceDto {
    private Double price;
    private String description;
    private Double duration;
    private UUID readerId;
    private UUID chapterId;
}
