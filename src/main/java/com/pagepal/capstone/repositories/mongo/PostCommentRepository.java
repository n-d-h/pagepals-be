package com.pagepal.capstone.repositories.mongo;


import com.pagepal.capstone.entities.mongo.PostComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostCommentRepository extends MongoRepository<PostComment, UUID> {
}
