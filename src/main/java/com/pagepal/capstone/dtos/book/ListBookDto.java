package com.pagepal.capstone.dtos.book;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListBookDto {
    private PagingDto pagination;
    private List<BookDto> list;
}
