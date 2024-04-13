package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.post.ListPostDto;
import com.pagepal.capstone.dtos.post.PostCreateDto;
import com.pagepal.capstone.dtos.post.PostDto;
import com.pagepal.capstone.dtos.post.PostUpdatedDto;
import com.pagepal.capstone.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @QueryMapping(name = "getAllPosts")
    public ListPostDto getAllPosts(
            @Argument(name = "page") Integer page,
            @Argument(name = "pageSize") Integer pageSize,
            @Argument(name = "sort") String sort
    ) {
        return postService.getAllPostsPagination(page, pageSize, sort);
    }

    @QueryMapping(name = "getAllPostsByReaderId")
    public ListPostDto getAllPostsByReaderId(
            @Argument(name = "readerId") UUID readerId,
            @Argument(name = "page") Integer page,
            @Argument(name = "pageSize") Integer pageSize,
            @Argument(name = "sort") String sort
    ) {
        return postService.getAllPostsByReaderId(readerId, page, pageSize, sort);
    }

    @QueryMapping(name = "getPostById")
    public PostDto getPostById(@Argument(name = "id") UUID id) {
        return postService.getPostById(id);
    }

    @MutationMapping(name = "savePost")
    public PostDto savePost(@Argument(name = "post") PostCreateDto dto) {
        return postService.savePost(dto);
    }

    @MutationMapping(name = "updatePost")
    public PostDto updatePost(@Argument(name = "id") UUID id, @Argument(name = "post") PostUpdatedDto dto) {
        return postService.updatePost(id, dto);
    }

    @MutationMapping(name = "deletePost")
    public PostDto deletePost(@Argument(name = "id") UUID id) {
        return postService.deletePost(id);
    }
}
