package com.pagepal.capstone.dtos.post;

import com.pagepal.capstone.entities.postgre.Reader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private UUID id;
    private String title;
    private String content;
    private Reader reader;
    private List<String> postImages;
    private Date createdAt;
}
