package com.pagepal.capstone.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostImageDto {
    private UUID id;
    private String imageUrl;
    private UUID postId;
}
