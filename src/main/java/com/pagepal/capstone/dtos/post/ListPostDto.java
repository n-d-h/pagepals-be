package com.pagepal.capstone.dtos.post;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListPostDto {
    private PagingDto pagination;
    private List<PostDto> list;
}
