package com.pagepal.capstone.entities.mongo;

import com.pagepal.capstone.enums.MessageStateEnum;
import com.pagepal.capstone.enums.MessageTypeEnum;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "MESSAGES")
public class Message {
    @Id
    private UUID id;

    @Field("type")
    private MessageTypeEnum type;

    @Field("attachment_url")
    private String attachmentUrl;

    @Field("created_at")
    private Date createdAt;

    @Field("deleted_at")
    private Date deletedAt;

    @Field("state")
    private MessageStateEnum state;

    private Conversation conversation;
}
