package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.post.PostCommentCreateDto;
import com.pagepal.capstone.dtos.post.PostCommentDto;
import com.pagepal.capstone.entities.postgre.Customer;
import com.pagepal.capstone.entities.postgre.Post;
import com.pagepal.capstone.entities.postgre.PostComment;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.repositories.CustomerRepository;
import com.pagepal.capstone.repositories.PostCommentRepository;
import com.pagepal.capstone.repositories.PostRepository;
import com.pagepal.capstone.repositories.ReaderRepository;
import com.pagepal.capstone.services.PostCommentService;
import com.pagepal.capstone.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostCommentServiceImpl implements PostCommentService {
    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final ReaderRepository readerRepository;
    private final CustomerRepository customerRepository;
    private final DateUtils dateUtils;

    @Override
    public List<PostCommentDto> getCommentByPostId(UUID postId) {
        List<PostCommentDto> res = new ArrayList<>();
        List<PostComment> postComments = postCommentRepository.findAllByPostId(postId);
        List<PostCommentDto> list = postComments.stream().map(this::toPostCommentDto).toList();
        for (PostCommentDto postCommentDto : list) {
            if (postCommentDto.getParentPostCommentId() == null) {
                postCommentDto.setId(postCommentDto.getId());
                postCommentDto.setContent(postCommentDto.getContent());
                postCommentDto.setCreatedAt(postCommentDto.getCreatedAt());
                postCommentDto.setUpdatedAt(postCommentDto.getUpdatedAt());
                postCommentDto.setPost(postCommentDto.getPost());
                postCommentDto.setReader(postCommentDto.getReader());
                postCommentDto.setCustomer(postCommentDto.getCustomer());
                postCommentDto.setParentPostCommentId(postCommentDto.getParentPostCommentId());
                List<PostCommentDto> children = getAllByParentPostCommentId(postCommentDto.getId());
                if(children.size() > 0) {
                    postCommentDto.setChildren(children);
                } else {
                    postCommentDto.setChildren(Collections.emptyList());
                }
            }
        }
        for (PostCommentDto postCommentDto : list) {
            if (postCommentDto.getParentPostCommentId() == null) {
                res.add(postCommentDto);
            }
        }
        return res;
    }

    @Override
    public PostCommentDto createComment(PostCommentCreateDto postCommentCreateDto) {
        Post post = postRepository.findById(postCommentCreateDto.getPostId()).orElse(null);
        if (post == null) {
            return null;
        }

        if(postCommentCreateDto.getParentPostCommentId() == null) {
            return createRootComment(postCommentCreateDto, post);
        } else {
            return createChildComment(postCommentCreateDto, post);
        }
    }

    @Override
    public PostCommentDto updateComment(String content, UUID postCommentId) {
        PostComment postComment = postCommentRepository.findById(postCommentId).orElse(null);
        if (postComment != null) {
            postComment.setContent(content);
            postCommentRepository.save(postComment);
            return toPostCommentDto(postComment);
        }
        return null;
    }

    @Override
    public PostCommentDto deleteComment(UUID postCommentId) {
        PostComment postComment = postCommentRepository.findById(postCommentId).orElse(null);
        if (postComment != null) {
            if (postComment.getParentPostComment() != null) {
                List<PostComment> children = postCommentRepository.findAllByParentPostCommentId(postCommentId);
                postCommentRepository.deleteAll(children);
            }
            postCommentRepository.delete(postComment);
            return toPostCommentDto(postComment);
        }
        return null;
    }

    private PostCommentDto createRootComment(PostCommentCreateDto postCommentCreateDto, Post post) {
        Reader reader = readerRepository.findById(postCommentCreateDto.getReaderId()).orElse(null);
        Customer customer = customerRepository.findById(postCommentCreateDto.getCustomerId()).orElse(null);

        PostComment postComment = new PostComment();
        postComment.setContent(postCommentCreateDto.getContent());
        postComment.setPost(post);
        postComment.setReader(reader);
        postComment.setCustomer(customer);
        postComment.setParentPostComment(null);
        postComment.setCreatedAt(dateUtils.getCurrentVietnamDate());
        postComment.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        postCommentRepository.save(postComment);
        return toPostCommentDto(postComment);
    }

    private PostCommentDto createChildComment(PostCommentCreateDto postCommentCreateDto, Post post) {
        Reader reader = readerRepository.findById(postCommentCreateDto.getReaderId()).orElse(null);
        Customer customer = customerRepository.findById(postCommentCreateDto.getCustomerId()).orElse(null);

        PostComment parentPostComment = postCommentRepository.findById(postCommentCreateDto.getParentPostCommentId()).orElse(null);
        if (parentPostComment == null) {
            return null;
        }

        PostComment postComment = new PostComment();
        postComment.setContent(postCommentCreateDto.getContent());
        postComment.setPost(post);
        postComment.setReader(reader);
        postComment.setCustomer(customer);
        postComment.setCreatedAt(dateUtils.getCurrentVietnamDate());
        postComment.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        postComment.setParentPostComment(parentPostComment);
        postCommentRepository.save(postComment);
        return toPostCommentDto(postComment);
    }

    private List<PostCommentDto> getAllByParentPostCommentId(UUID parentPostCommentId) {
        return postCommentRepository.findAllByParentPostCommentId(parentPostCommentId).stream().map(this::toPostCommentDto).toList();
    }

    private PostCommentDto toPostCommentDto(PostComment postComment) {
        PostCommentDto postCommentDto = new PostCommentDto();
        postCommentDto.setId(postComment.getId());
        postCommentDto.setContent(postComment.getContent());
        postCommentDto.setCreatedAt(postComment.getCreatedAt().toString());
        postCommentDto.setUpdatedAt(postComment.getUpdatedAt().toString());
        postCommentDto.setPost(postComment.getPost());
        postCommentDto.setReader(postComment.getReader());
        postCommentDto.setCustomer(postComment.getCustomer());
        postCommentDto.setChildren(getAllByParentPostCommentId(postComment.getId()));
        return postCommentDto;
    }
}
