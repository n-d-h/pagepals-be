package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.book.BookDto;
import com.pagepal.capstone.dtos.book.ListBookDto;
import com.pagepal.capstone.dtos.book.WriteBookDto;

import java.util.UUID;

public interface BookService {
    ListBookDto getListBook(String search, String sort, String author, UUID categoryId,  Integer page, Integer pageSize);
    BookDto createBook(WriteBookDto bookDto);
    BookDto bookById(UUID id);
}
