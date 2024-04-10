package com.pagepal.capstone.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentDto {
    private UUID id;
    private String content;
    private String createdAt;
    private String updatedAt;
    private UUID postId;
    private UUID readerId;
    private UUID customerId;
    private UUID parentPostCommentId;
}
