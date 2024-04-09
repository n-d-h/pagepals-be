package com.pagepal.capstone.dtos.reader;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReaderBookListDto {
    private List<ReaderBookDto> list;
    private PagingDto paging;
}
