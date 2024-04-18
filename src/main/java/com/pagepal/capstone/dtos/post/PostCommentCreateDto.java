package com.pagepal.capstone.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentCreateDto {
    private String content;
    private UUID postId;
    private UUID readerId;
    private UUID customerId;
    private UUID parentPostCommentId;
}
