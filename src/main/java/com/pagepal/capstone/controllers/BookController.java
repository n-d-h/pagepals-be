package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.book.BookDto;
import com.pagepal.capstone.dtos.book.BookQueryDto;
import com.pagepal.capstone.dtos.book.ListBookDto;
import com.pagepal.capstone.dtos.book.WriteBookDto;
import com.pagepal.capstone.services.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @QueryMapping(name = "getListBook")
    public ListBookDto getListBook(@Argument(name = "query") @Valid BookQueryDto query) {
        return bookService.getListBook(
                query.getSearch(),
                query.getSort(),
                query.getAuthor(),
                query.getCategoryId(),
                query.getPage(),
                query.getPageSize());
    }

    @MutationMapping(name = "createBook")
    public BookDto createBook(@Argument(name = "book") WriteBookDto bookDto) {
        return bookService.createBook(bookDto);
    }

    @QueryMapping(name = "bookById")
    public BookDto bookById(@Argument(name = "id") UUID id) {
        return bookService.bookById(id);
    }

//    @Argument(name = "search") Optional<String> search,
//    @Argument(name = "sort") Optional<String> sort,
//    @Argument(name = "filter") Optional<String> filter,
//    @Argument(name = "page") Optional<Integer> page,
//    @Argument(name = "pageSize") Optional<Integer> pageSize
}
