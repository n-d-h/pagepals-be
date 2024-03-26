package com.pagepal.capstone.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateDto {
    private String title;
    private String content;
    private UUID readerId;
    private List<String> postImages;
}
