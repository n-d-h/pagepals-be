package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.post.PostCommentCreateDto;
import com.pagepal.capstone.dtos.post.PostCommentDto;

import java.util.List;
import java.util.UUID;

public interface PostCommentService {
    List<PostCommentDto> getCommentByPostId(UUID postId);
    PostCommentDto createComment(PostCommentCreateDto postCommentCreateDto);
    PostCommentDto updateComment(String content, UUID postCommentId);
    PostCommentDto deleteComment(UUID postCommentId);
}
