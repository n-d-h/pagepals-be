package com.pagepal.capstone.dtos.book;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListBookDto {
    private PagingDto pagination;
    private List<BookDto> list;
}
