package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.book.BookDto;
import com.pagepal.capstone.dtos.book.ListBookDto;

import java.util.UUID;

public interface BookService {

    ListBookDto getListBookForCustomer(String search, String sort, String author, UUID categoryId, Integer page, Integer pageSize);

    BookDto getBookById(UUID id);
}
