package com.pagepal.capstone.dtos.post;

import com.pagepal.capstone.entities.postgre.Customer;
import com.pagepal.capstone.entities.postgre.Post;
import com.pagepal.capstone.entities.postgre.Reader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentDto {
    private UUID id;
    private String content;
    private String createdAt;
    private String updatedAt;
    private Post post;
    private Reader reader;
    private Customer customer;
    private UUID parentPostCommentId;
    private List<PostCommentDto> children;
}
