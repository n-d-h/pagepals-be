package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.book.BookDto;
import com.pagepal.capstone.dtos.book.WriteBookDto;
import com.pagepal.capstone.entities.postgre.Book;
import com.pagepal.capstone.entities.postgre.Category;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.BookMapper;
import com.pagepal.capstone.repositories.postgre.BookRepository;
import com.pagepal.capstone.repositories.postgre.CategoryRepository;
import com.pagepal.capstone.services.BookService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
//    private final BookMapper bookMapper;

    @Override
    public List<BookDto> getListBook(String search, String sort, String author, UUID categoryId, Integer page, Integer pageSize) {
        if (page == null || page < 0)
            page = 0;

        if (pageSize == null || pageSize < 0)
            pageSize = 10;

        Pageable pageable;
        if (sort != null && sort.equals("desc")) {
            pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        } else {
            pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").ascending());
        }

        if (search == null)
            search = "";

        if (author == null)
            author = "";

        Page<Book> bookPage;
        if (categoryId == null)
            bookPage = bookRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(search, author, pageable);
        else {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Category not found"));
            bookPage = bookRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndCategory(search, author, category, pageable);
        }

        if (bookPage == null) return Collections.emptyList();
        else return bookPage.map(BookMapper.INSTANCE::toDto).toList();
    }

    @Override
    public BookDto createBook(WriteBookDto bookDto) {
        Book book = BookMapper.INSTANCE.createBook(bookDto);
        book.setId(null);
        book.setStatus(Status.ACTIVE);
        book.setCreatedAt(new Date());
        return BookMapper.INSTANCE.toDto(bookRepository.save(book));
    }

    public BookDto bookById(UUID id) {
        return BookMapper.INSTANCE.toDto(bookRepository.findById(id).orElse(null));
    }
}
