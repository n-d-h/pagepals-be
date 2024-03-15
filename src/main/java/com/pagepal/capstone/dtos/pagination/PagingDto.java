package com.pagepal.capstone.dtos.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PagingDto {
    private Integer totalOfPages;
    private Long totalOfElements;
    private String sort;
    private Integer currentPage;
    private Integer pageSize;
}
