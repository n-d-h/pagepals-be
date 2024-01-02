package com.pagepal.capstone.entities.mongo;

import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.enums.Status;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "POSTS")
public class Post {
    @Id
    private UUID id;

    @Field("title")
    private String title;

    @Field("content")
    private String content;

    @Field("image_url")
    private String imageUrl;

    @Field("created_at")
    private Date createdAt;

    @Field("updated_at")
    private Date updatedAt;

    @Field("status")
    private Status status;

    private Reader reader;

    private List<PostTag> postTags;

    private List<PostComment> postComments;
}
