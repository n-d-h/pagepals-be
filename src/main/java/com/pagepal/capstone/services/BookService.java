package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.book.BookDto;
import com.pagepal.capstone.dtos.book.WriteBookDto;

import java.util.List;
import java.util.UUID;

public interface BookService {
    List<BookDto> getListBook(String search, String sort, String filter, Integer page, Integer pageSize);

    BookDto createBook(WriteBookDto bookDto);
    BookDto bookById(UUID id);
}
