package com.pagepal.capstone.dtos.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingDto {
    private Integer totalOfPages;
    private Long totalOfElements;
    private String sort;
    private Integer currentPage;
    private Integer pageSize;
}
