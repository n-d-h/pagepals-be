package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.googlebook.BookSearchResult;
import com.pagepal.capstone.services.BookService;
import com.pagepal.capstone.services.GoogleBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final GoogleBookService googleBookService;

    @QueryMapping(name = "searchBook")
    public BookSearchResult searchBook(@Argument(name ="title") String title,
                                        @Argument(name = "author") String author,
                                      @Argument(name = "page") Integer page,
                                      @Argument(name = "pageSize") Integer pageSize) {
        return googleBookService.searchBook(title, author, page, pageSize);
    }

}
