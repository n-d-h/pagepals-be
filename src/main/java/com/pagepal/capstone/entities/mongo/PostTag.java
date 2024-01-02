package com.pagepal.capstone.entities.mongo;

import com.pagepal.capstone.enums.Status;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "POST_TAGS")
public class PostTag {
    @Id
    private UUID id;

    @Field("title")
    private String title;

    @Field("content")
    private String content;

    @Field("status")
    private Status status;

    private List<Post> posts;
}
