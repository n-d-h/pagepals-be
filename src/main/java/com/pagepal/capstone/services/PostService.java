package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.post.PostCreateDto;
import com.pagepal.capstone.dtos.post.PostDto;
import com.pagepal.capstone.dtos.post.PostUpdatedDto;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<PostDto> getAllPosts();
    PostDto getPostById(UUID id);
    PostDto savePost(PostCreateDto dto);
    PostDto updatePost(UUID id, PostUpdatedDto postUpdateDto);
    PostDto deletePost(UUID id);
}
