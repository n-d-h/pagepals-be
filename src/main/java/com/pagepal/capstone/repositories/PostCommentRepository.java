package com.pagepal.capstone.repositories;


import com.pagepal.capstone.entities.postgre.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, UUID> {
    List<PostComment> findAllByPostId(UUID postId);
    List<PostComment> findAllByParentPostCommentId(UUID parentPostCommentId);
}
