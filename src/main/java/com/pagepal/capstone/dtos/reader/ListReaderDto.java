package com.pagepal.capstone.dtos.reader;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListReaderDto {
    private PagingDto pagination;
    private List<ReaderDto> list;
}
