package com.pagepal.capstone.dtos.reader;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListReaderReviewDto {
    private List<ReaderReviewDto> list;
    private PagingDto pagination;
}
