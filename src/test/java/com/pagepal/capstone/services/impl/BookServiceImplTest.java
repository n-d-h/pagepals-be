package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.book.BookDto;
import com.pagepal.capstone.dtos.book.WriteBookDto;
import com.pagepal.capstone.entities.postgre.Book;
import com.pagepal.capstone.entities.postgre.Category;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.postgre.BookRepository;
import com.pagepal.capstone.repositories.postgre.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ContextConfiguration(classes = {BookServiceImpl.class})
@ExtendWith(SpringExtension.class)
class BookServiceImplTest {
    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private BookServiceImpl bookServiceImpl;

    //Mock data
    //Date
    LocalDate localDate1 = LocalDate.of(2023, 12, 21);
    LocalDate localDate2 = LocalDate.of(2022, 12, 21);
    LocalDate localDate3 = LocalDate.of(2021, 12, 21);
    Date date1 = java.sql.Date.valueOf(localDate1);
    Date date2 = java.sql.Date.valueOf(localDate2);
    Date date3 = java.sql.Date.valueOf(localDate3);

    //Category
    Category category1 = new Category(UUID.fromString("123e4237-e89b-12d3-a456-422233474000"), "category1", "des1", Status.ACTIVE, null);
    Category category2 = new Category(UUID.randomUUID(), "category2", "des2", Status.ACTIVE, null);
    Category category3 = new Category();

    //Book
    Book book1 = new Book(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "book1",
            "book1mock", "author1", "publisher1", 123L,
            "vietnamese", null, null, null,
            date1, Status.ACTIVE, category1);

    Book book2 = new Book(UUID.randomUUID(), "book2",
            "book2mock", "author2", "publisher2", 234L,
            "english", null, null, null,
            date2, Status.ACTIVE, category1);

    Book book3 = new Book(UUID.randomUUID(), "book3",
            "book3mock", "author3", "publisher3", 345L,
            "vietnamese", null, null, null,
            date3, Status.ACTIVE, category2);

    /**
     * Method under test: {@link BookServiceImpl#getListBook(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBook() {
        List<Book> bookList = Arrays.asList(book1, book2);
        Page<Book> bookPage = new PageImpl<>(bookList, PageRequest.of(0, 10), 3);
        when(categoryRepository.findById(category1.getId())).thenReturn(java.util.Optional.of(category1));
        when(bookRepository
                .findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndCategory("", "", category1, PageRequest.of(0, 10, Sort.by("createdAt").ascending())))
                .thenReturn(bookPage);
        List<BookDto> list = bookServiceImpl.getListBook("", "asc", "", category1.getId(), 0, 10);
        assertEquals(2, list.size());
        verify(bookRepository).findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndCategory("", "", category1, PageRequest.of(0, 10, Sort.by("createdAt").ascending()));
    }

    @Test
    void canGetListBookWithNullCategory() {
        List<Book> bookList = Arrays.asList(book1, book2);
        Page<Book> bookPage = new PageImpl<>(bookList, PageRequest.of(0, 10), 3);
        when(bookRepository
                .findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase("", "", PageRequest.of(0, 10, Sort.by("createdAt").ascending())))
                .thenReturn(bookPage);
        List<BookDto> list = bookServiceImpl.getListBook("", "asc", "", null, 0, 10);
        assertEquals(2, list.size());
        verify(bookRepository).findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase("", "", PageRequest.of(0, 10, Sort.by("createdAt").ascending()));
    }

    /**
     * Method under test: {@link BookServiceImpl#bookById(UUID)}
     */

    @Test
    void canGetBookWithId() {
        when(bookRepository.findById(book1.getId())).thenReturn(java.util.Optional.of(book1));
        BookDto bookDto = bookServiceImpl.bookById(book1.getId());
        assertEquals(bookDto.getTitle(), book1.getTitle());
        verify(bookRepository).findById(book1.getId());
    }

    @Test
    void shouldReturnNullWhenNotFoundBookById() {
        when(bookRepository.findById(book1.getId())).thenReturn(java.util.Optional.empty());
        BookDto bookDto = bookServiceImpl.bookById(book1.getId());
        assertNull(bookDto);
        verify(bookRepository).findById(book1.getId());
    }

    /**
     * Method under test: {@link BookServiceImpl#createBook(WriteBookDto)}
     */
    @Test
    void canSaveNewBook() {
//        WriteBookDto writeBookDto = new WriteBookDto();
//        writeBookDto.setTitle("title");
//        writeBookDto.setAuthor("author");
//        writeBookDto.setPublisher("publisher");
//        writeBookDto.setPages(123L);
//        writeBookDto.setLanguage("vietnamese");
//        writeBookDto.setCategoryId(category1.getId());
//
//        Book book = new Book();
//        book.setId(book1.getId());
//        book.setTitle("title");
//        book.setAuthor("author");
//        book.setPublisher("publisher");
//        book.setPages(123L);
//        book.setLanguage("vietnamese");
//        book.setCategory(category1);
//        book.setStatus(Status.ACTIVE);
//        book.setCreatedAt(new Date());
//        book.setCategory(category1);
//
//        when(categoryRepository.findById(category1.getId())).thenReturn(java.util.Optional.of(category1));
//
//        when(bookRepository.save(book)).thenReturn(book);
//        when(bookRepository.findById(book.getId())).thenReturn(java.util.Optional.of(book));
//        BookDto bookDto = bookServiceImpl.createBook(writeBookDto);
////        assertEquals(bookDto.getTitle(), book.getTitle());
//        verify(bookRepository).save(book);
    }
}
