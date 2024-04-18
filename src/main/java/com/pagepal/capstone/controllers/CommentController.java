package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.post.PostCommentCreateDto;
import com.pagepal.capstone.dtos.post.PostCommentDto;
import com.pagepal.capstone.services.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final PostCommentService postCommentService;

    @QueryMapping("getCommentByPostId")
    public List<PostCommentDto> getCommentByPostId(@Argument("postId") UUID postId) {
        return postCommentService.getCommentByPostId(postId);
    }

    @MutationMapping("createComment")
    public PostCommentDto createComment(@Argument("input") PostCommentCreateDto postCommentCreateDto) {
        return postCommentService.createComment(postCommentCreateDto);
    }

    @MutationMapping("updateComment")
    public PostCommentDto updateComment(@Argument("content") String content, @Argument("id") UUID postCommentId) {
        return postCommentService.updateComment(content, postCommentId);
    }

    @MutationMapping("deleteComment")
    public PostCommentDto deleteComment(@Argument("id") UUID postCommentId) {
        return postCommentService.deleteComment(postCommentId);
    }
}
