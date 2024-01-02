package com.pagepal.capstone.repositories.mongo;

import com.pagepal.capstone.entities.mongo.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends MongoRepository<Post, UUID> {
}
