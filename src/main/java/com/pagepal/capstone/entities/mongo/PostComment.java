package com.pagepal.capstone.entities.mongo;

import com.pagepal.capstone.enums.Status;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "POST_COMMENTS")
public class PostComment {
    @Id
    private UUID id;

    @Field("content")
    private String content;

    @Field("created_at")
    private Date createdAt;

    @Field("updated_at")
    private Date updatedAt;

    @Field("status")
    private Status status;

    private PostComment parentPostComment;
}
