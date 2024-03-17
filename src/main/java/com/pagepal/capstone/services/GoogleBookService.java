package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.googlebook.BookSearchResult;

public interface GoogleBookService {
    BookSearchResult searchBook(String query, Integer page, Integer pageSize);
}
