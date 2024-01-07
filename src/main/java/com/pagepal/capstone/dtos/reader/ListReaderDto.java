package com.pagepal.capstone.dtos.reader;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListReaderDto {
    private PagingDto pagination;
    private List<ReaderDto> list;
}
