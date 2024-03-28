package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.post.PostCreateDto;
import com.pagepal.capstone.dtos.post.PostDto;
import com.pagepal.capstone.dtos.post.PostUpdatedDto;
import com.pagepal.capstone.entities.postgre.Post;
import com.pagepal.capstone.entities.postgre.PostImage;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.PostImageRepository;
import com.pagepal.capstone.repositories.PostRepository;
import com.pagepal.capstone.services.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    @Secured({"READER"})
    @Override
    public List<PostDto> getAllPosts() {
        var listPosts = postRepository.findAll();
        List<PostDto> listPostDtos = new ArrayList<>();
        listPosts.forEach(post -> listPostDtos.add(
                PostDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .readerId(post.getReader().getId())
                        .postImages(post.getPostImages().stream().map(PostImage::getImageUrl).toList())
                        .build()
        ));
        return listPostDtos;
    }

    @Secured({"READER"})
    @Override
    public PostDto getPostById(UUID id) {
        var post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return null;
        }
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .readerId(post.getReader().getId())
                .postImages(post.getPostImages().stream().map(PostImage::getImageUrl).toList())
                .build();
    }

    @Secured({"READER"})
    @Override
    public PostDto savePost(PostCreateDto postDto) {
        Reader reader = new Reader();
        reader.setId(postDto.getReaderId());

        Post savedPost = new Post();
        savedPost.setTitle(postDto.getTitle());
        savedPost.setContent(postDto.getContent());
        savedPost.setReader(reader);
        savedPost.setCreatedAt(new Date());
        savedPost.setUpdatedAt(new Date());
        savedPost.setStatus(Status.ACTIVE);
        postRepository.save(savedPost);

        List<String> postImages = postDto.getPostImages();
        postImages.forEach(postImage -> {
            PostImage savedPostImage = new PostImage();
            savedPostImage.setImageUrl(postImage);
            savedPostImage.setPost(savedPost);
            postImageRepository.save(savedPostImage);
        });

        PostDto postDtoSaved = PostDto.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .readerId(savedPost.getReader().getId())
                .postImages(postImages)
                .build();

        return postDtoSaved;
    }

    @Secured({"READER"})
    @Override
    public PostDto updatePost(UUID id, PostUpdatedDto postUpdatedDto) {
        var post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return null;
        }
        post.setTitle(postUpdatedDto.getTitle());
        post.setContent(postUpdatedDto.getContent());
        post.setUpdatedAt(new Date());
        post.setStatus(Status.ACTIVE);
        postRepository.save(post);

        // delete all old post
        postImageRepository.deleteAll(post.getPostImages());

        List<String> postImages = postUpdatedDto.getPostImages();
        post.setPostImages(postImages.stream().map(imageUrl -> {
            PostImage postImage = new PostImage();
            postImage.setImageUrl(imageUrl);
            postImage.setPost(post);
            postImageRepository.save(postImage);
            return postImage;
        }).toList());


        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .readerId(post.getReader().getId())
                .postImages(post.getPostImages().stream().map(PostImage::getImageUrl).toList())
                .build();
    }

    @Secured({"READER"})
    @Override
    public PostDto deletePost(UUID id) {
        var post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return null;
        }
        post.setStatus(Status.INACTIVE);
        postRepository.save(post);

        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .readerId(post.getReader().getId())
                .postImages(new ArrayList<>())
                .build();
    }
}
