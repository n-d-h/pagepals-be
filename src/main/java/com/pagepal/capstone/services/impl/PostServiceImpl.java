package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.dtos.post.ListPostDto;
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
import com.pagepal.capstone.utils.DateUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final DateUtils dateUtils;

    @Override
    public List<PostDto> getAllPosts() {
        var listPosts = postRepository.findAll();
        List<PostDto> listPostDtos = new ArrayList<>();
        listPosts.forEach(post -> listPostDtos.add(
                PostDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .reader(post.getReader())
                        .postImages(post.getPostImages().stream().map(PostImage::getImageUrl).toList())
                        .build()
        ));
        return listPostDtos;
    }

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
                .reader(post.getReader())
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
        savedPost.setCreatedAt(dateUtils.getCurrentVietnamDate());
        savedPost.setUpdatedAt(dateUtils.getCurrentVietnamDate());
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
                .reader(savedPost.getReader())
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
        post.setUpdatedAt(dateUtils.getCurrentVietnamDate());
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
                .reader(post.getReader())
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
                .reader(post.getReader())
                .postImages(new ArrayList<>())
                .build();
    }

    @Override
    public ListPostDto getAllPostsByReaderId(UUID readerId, Integer page, Integer pageSize, String sort) {
        Pageable pageable = createPageable(page, pageSize, sort);

        Page<Post> posts = postRepository.findAllByReaderId(readerId, pageable);

        return mapPostsToDto(posts);
    }

    @Override
    public ListPostDto getAllPostsPagination(Integer page, Integer pageSize, String sort) {
        Pageable pageable = createPageable(page, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        return mapPostsToDto(posts);
    }

    private Pageable createPageable(Integer page, Integer pageSize, String sort) {
        if (page == null || page < 0)
            page = 0;

        if (pageSize == null || pageSize < 0)
            pageSize = 10;

        if (sort != null && sort.equals("desc")) {
            return PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        } else {
            return PageRequest.of(page, pageSize, Sort.by("createdAt").ascending());
        }
    }

    private ListPostDto mapPostsToDto(Page<Post> posts) {
        var listPostDto = new ListPostDto();

        if(posts == null) {
            listPostDto.setList(Collections.emptyList());
            listPostDto.setPagination(null);
            return listPostDto;
        } else {
            var pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(posts.getTotalPages());
            pagingDto.setTotalOfElements(posts.getTotalElements());
            pagingDto.setSort(posts.getSort().toString());
            pagingDto.setCurrentPage(posts.getNumber());
            pagingDto.setPageSize(posts.getSize());

            listPostDto.setList(posts.map(post -> PostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .reader(post.getReader())
                    .postImages(post.getPostImages().stream().map(PostImage::getImageUrl).toList())
                    .createdAt(dateUtils.getCurrentVietnamDate())
                    .build()).toList());
            listPostDto.setPagination(pagingDto);
            return listPostDto;
        }
    }
}
