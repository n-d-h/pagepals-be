package com.pagepal.capstone.dtos.post;

import com.pagepal.capstone.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostTagDto {
    private UUID id;
    private String title;
    private String content;
    private Status status;
}
