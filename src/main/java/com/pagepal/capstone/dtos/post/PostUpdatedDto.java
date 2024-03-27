package com.pagepal.capstone.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdatedDto {
    private String title;
    private String content;
    private List<String> postImages;
}
