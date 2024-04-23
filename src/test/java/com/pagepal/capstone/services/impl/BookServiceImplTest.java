package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.book.BookDto;
import com.pagepal.capstone.dtos.book.ListBookDto;
import com.pagepal.capstone.dtos.googlebook.GoogleBook;
import com.pagepal.capstone.dtos.googlebook.ImageLinks;
import com.pagepal.capstone.dtos.googlebook.VolumeInfo;
import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.entities.postgre.Author;
import com.pagepal.capstone.entities.postgre.Book;
import com.pagepal.capstone.entities.postgre.Category;
import com.pagepal.capstone.repositories.AuthorRepository;
import com.pagepal.capstone.repositories.BookRepository;
import com.pagepal.capstone.repositories.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {BookServiceImpl.class})
@ExtendWith(SpringExtension.class)
class BookServiceImplTest {
    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private BookServiceImpl bookServiceImpl;

    @MockBean
    private CategoryRepository categoryRepository;

    /**
     * Method under test: {@link BookServiceImpl#getListBookForCustomer(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBookForCustomer() {
        when(bookRepository.searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(categoryRepository.findById((UUID) any())).thenReturn(Optional.of(new Category()));
        when(authorRepository.findByNameContainingIgnoreCase((String) any())).thenReturn(new ArrayList<>());
        ListBookDto actualListBookForCustomer = bookServiceImpl.getListBookForCustomer("Search", "Sort", "JaneDoe",
                UUID.randomUUID(), 1, 3);
        assertTrue(actualListBookForCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookForCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookRepository).searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any());
        verify(categoryRepository).findById((UUID) any());
        verify(authorRepository).findByNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#getListBookForCustomer(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBookForCustomer2() {
        when(bookRepository.searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(categoryRepository.findById((UUID) any())).thenReturn(Optional.of(new Category()));
        when(authorRepository.findByNameContainingIgnoreCase((String) any()))
                .thenThrow(new EntityNotFoundException("An error occurred"));
        assertThrows(EntityNotFoundException.class,
                () -> bookServiceImpl.getListBookForCustomer("Search", "Sort", "JaneDoe", UUID.randomUUID(), 1, 3));
        verify(authorRepository).findByNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#getListBookForCustomer(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBookForCustomer3() {
        when(bookRepository.searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any())).thenReturn(null);
        when(categoryRepository.findById((UUID) any())).thenReturn(Optional.of(new Category()));
        when(authorRepository.findByNameContainingIgnoreCase((String) any())).thenReturn(new ArrayList<>());
        ListBookDto actualListBookForCustomer = bookServiceImpl.getListBookForCustomer("Search", "Sort", "JaneDoe",
                UUID.randomUUID(), 1, 3);
        assertTrue(actualListBookForCustomer.getList().isEmpty());
        assertNull(actualListBookForCustomer.getPagination());
        verify(bookRepository).searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any());
        verify(categoryRepository).findById((UUID) any());
        verify(authorRepository).findByNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#getListBookForCustomer(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBookForCustomer5() {
        when(bookRepository.searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(categoryRepository.findById((UUID) any())).thenReturn(Optional.empty());
        when(authorRepository.findByNameContainingIgnoreCase((String) any())).thenReturn(new ArrayList<>());
        assertThrows(EntityNotFoundException.class,
                () -> bookServiceImpl.getListBookForCustomer("Search", "Sort", "JaneDoe", UUID.randomUUID(), 1, 3));
        verify(categoryRepository).findById((UUID) any());
        verify(authorRepository).findByNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#getListBookForCustomer(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBookForCustomer6() {
        when(bookRepository.searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(categoryRepository.findById((UUID) any())).thenReturn(Optional.of(new Category()));
        when(authorRepository.findByNameContainingIgnoreCase((String) any())).thenReturn(new ArrayList<>());
        ListBookDto actualListBookForCustomer = bookServiceImpl.getListBookForCustomer(null, "Sort", "JaneDoe",
                UUID.randomUUID(), 1, 3);
        assertTrue(actualListBookForCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookForCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookRepository).searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any());
        verify(categoryRepository).findById((UUID) any());
        verify(authorRepository).findByNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#getListBookForCustomer(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBookForCustomer7() {
        when(bookRepository.searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(categoryRepository.findById((UUID) any())).thenReturn(Optional.of(new Category()));
        when(authorRepository.findByNameContainingIgnoreCase((String) any())).thenReturn(new ArrayList<>());
        ListBookDto actualListBookForCustomer = bookServiceImpl.getListBookForCustomer("Search", "desc", "JaneDoe",
                UUID.randomUUID(), 1, 3);
        assertTrue(actualListBookForCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookForCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookRepository).searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any());
        verify(categoryRepository).findById((UUID) any());
        verify(authorRepository).findByNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#getListBookForCustomer(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBookForCustomer8() {
        when(bookRepository.searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(categoryRepository.findById((UUID) any())).thenReturn(Optional.of(new Category()));
        when(authorRepository.findByNameContainingIgnoreCase((String) any())).thenReturn(new ArrayList<>());
        ListBookDto actualListBookForCustomer = bookServiceImpl.getListBookForCustomer("Search", null, "JaneDoe",
                UUID.randomUUID(), 1, 3);
        assertTrue(actualListBookForCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookForCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookRepository).searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any());
        verify(categoryRepository).findById((UUID) any());
        verify(authorRepository).findByNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#getListBookForCustomer(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBookForCustomer9() {
        when(bookRepository.searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(categoryRepository.findById((UUID) any())).thenReturn(Optional.of(new Category()));
        when(authorRepository.findByNameContainingIgnoreCase((String) any())).thenReturn(new ArrayList<>());
        ListBookDto actualListBookForCustomer = bookServiceImpl.getListBookForCustomer("Search", "Sort", null,
                UUID.randomUUID(), 1, 3);
        assertTrue(actualListBookForCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookForCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookRepository).searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any());
        verify(categoryRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#getListBookForCustomer(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBookForCustomer10() {
        when(bookRepository.searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(categoryRepository.findById((UUID) any())).thenReturn(Optional.of(new Category()));
        when(authorRepository.findByNameContainingIgnoreCase((String) any())).thenReturn(new ArrayList<>());
        ListBookDto actualListBookForCustomer = bookServiceImpl.getListBookForCustomer("Search", "Sort", "",
                UUID.randomUUID(), 1, 3);
        assertTrue(actualListBookForCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookForCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookRepository).searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any());
        verify(categoryRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#getListBookForCustomer(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBookForCustomer11() {
        when(bookRepository.findByTitleContainingIgnoreCase((String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookRepository.searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(categoryRepository.findById((UUID) any())).thenReturn(Optional.of(new Category()));
        when(authorRepository.findByNameContainingIgnoreCase((String) any())).thenReturn(new ArrayList<>());
        ListBookDto actualListBookForCustomer = bookServiceImpl.getListBookForCustomer("Search", "Sort", "JaneDoe", null,
                1, 3);
        assertTrue(actualListBookForCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookForCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookRepository).findByTitleContainingIgnoreCase((String) any(), (Pageable) any());
        verify(authorRepository).findByNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#getListBookForCustomer(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBookForCustomer12() {
        when(bookRepository.searchBooksByAuthorAndTitle((List<Author>) any(), (String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookRepository.findByTitleContainingIgnoreCase((String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookRepository.searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(categoryRepository.findById((UUID) any())).thenReturn(Optional.of(new Category()));

        ArrayList<Author> authorList = new ArrayList<>();
        authorList.add(new Author());
        when(authorRepository.findByNameContainingIgnoreCase((String) any())).thenReturn(authorList);
        ListBookDto actualListBookForCustomer = bookServiceImpl.getListBookForCustomer("Search", "Sort", "JaneDoe", null,
                1, 3);
        assertTrue(actualListBookForCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookForCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookRepository).searchBooksByAuthorAndTitle((List<Author>) any(), (String) any(), (Pageable) any());
        verify(authorRepository).findByNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#getListBookForCustomer(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBookForCustomer13() {
        when(bookRepository.searchBooksByAuthorAndTitle((List<Author>) any(), (String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookRepository.findByTitleContainingIgnoreCase((String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookRepository.searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(categoryRepository.findById((UUID) any())).thenReturn(Optional.of(new Category()));

        ArrayList<Author> authorList = new ArrayList<>();
        authorList.add(new Author());
        when(authorRepository.findByNameContainingIgnoreCase((String) any())).thenReturn(authorList);
        ListBookDto actualListBookForCustomer = bookServiceImpl.getListBookForCustomer("Search", "Sort", null, null, 1,
                3);
        assertTrue(actualListBookForCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookForCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookRepository).findByTitleContainingIgnoreCase((String) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#getListBookForCustomer(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBookForCustomer14() {
        when(bookRepository.searchBooksByAuthorAndTitle((List<Author>) any(), (String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookRepository.findByTitleContainingIgnoreCase((String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookRepository.searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(categoryRepository.findById((UUID) any())).thenReturn(Optional.of(new Category()));

        ArrayList<Author> authorList = new ArrayList<>();
        authorList.add(new Author());
        when(authorRepository.findByNameContainingIgnoreCase((String) any())).thenReturn(authorList);
        ListBookDto actualListBookForCustomer = bookServiceImpl.getListBookForCustomer("Search", "Sort", "JaneDoe", null,
                null, 3);
        assertTrue(actualListBookForCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookForCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookRepository).searchBooksByAuthorAndTitle((List<Author>) any(), (String) any(), (Pageable) any());
        verify(authorRepository).findByNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#getListBookForCustomer(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBookForCustomer15() {
        when(bookRepository.searchBooksByAuthorAndTitle((List<Author>) any(), (String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookRepository.findByTitleContainingIgnoreCase((String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookRepository.searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(categoryRepository.findById((UUID) any())).thenReturn(Optional.of(new Category()));

        ArrayList<Author> authorList = new ArrayList<>();
        authorList.add(new Author());
        when(authorRepository.findByNameContainingIgnoreCase((String) any())).thenReturn(authorList);
        ListBookDto actualListBookForCustomer = bookServiceImpl.getListBookForCustomer("Search", "Sort", "JaneDoe", null,
                -1, 3);
        assertTrue(actualListBookForCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookForCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookRepository).searchBooksByAuthorAndTitle((List<Author>) any(), (String) any(), (Pageable) any());
        verify(authorRepository).findByNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#getListBookForCustomer(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBookForCustomer17() {
        when(bookRepository.searchBooksByAuthorAndTitle((List<Author>) any(), (String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookRepository.findByTitleContainingIgnoreCase((String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookRepository.searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(categoryRepository.findById((UUID) any())).thenReturn(Optional.of(new Category()));

        ArrayList<Author> authorList = new ArrayList<>();
        authorList.add(new Author());
        when(authorRepository.findByNameContainingIgnoreCase((String) any())).thenReturn(authorList);
        ListBookDto actualListBookForCustomer = bookServiceImpl.getListBookForCustomer("Search", "Sort", "JaneDoe", null,
                1, null);
        assertTrue(actualListBookForCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookForCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookRepository).searchBooksByAuthorAndTitle((List<Author>) any(), (String) any(), (Pageable) any());
        verify(authorRepository).findByNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#getListBookForCustomer(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBookForCustomer18() {
        when(bookRepository.searchBooksByAuthorAndTitle((List<Author>) any(), (String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookRepository.findByTitleContainingIgnoreCase((String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookRepository.searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(categoryRepository.findById((UUID) any())).thenReturn(Optional.of(new Category()));

        ArrayList<Author> authorList = new ArrayList<>();
        authorList.add(new Author());
        when(authorRepository.findByNameContainingIgnoreCase((String) any())).thenReturn(authorList);
        ListBookDto actualListBookForCustomer = bookServiceImpl.getListBookForCustomer("Search", "Sort", "JaneDoe", null,
                1, -1);
        assertTrue(actualListBookForCustomer.getList().isEmpty());
        PagingDto pagination = actualListBookForCustomer.getPagination();
        assertEquals(0, pagination.getCurrentPage().intValue());
        assertEquals(1, pagination.getTotalOfPages().intValue());
        assertEquals(0L, pagination.getTotalOfElements().longValue());
        assertEquals("UNSORTED", pagination.getSort());
        assertEquals(0, pagination.getPageSize().intValue());
        verify(bookRepository).searchBooksByAuthorAndTitle((List<Author>) any(), (String) any(), (Pageable) any());
        verify(authorRepository).findByNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#getListBookForCustomer(String, String, String, UUID, Integer, Integer)}
     */
    @Test
    void canGetListBookForCustomer19() {
        when(bookRepository.searchBooksByAuthorAndTitle((List<Author>) any(), (String) any(), (Pageable) any()))
                .thenThrow(new EntityNotFoundException("An error occurred"));
        when(bookRepository.findByTitleContainingIgnoreCase((String) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(bookRepository.searchBooksByAuthorCategoryAndTitle((List<Author>) any(), (List<Category>) any(),
                (String) any(), (Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(categoryRepository.findById((UUID) any())).thenReturn(Optional.of(new Category()));

        ArrayList<Author> authorList = new ArrayList<>();
        authorList.add(new Author());
        when(authorRepository.findByNameContainingIgnoreCase((String) any())).thenReturn(authorList);
        assertThrows(EntityNotFoundException.class,
                () -> bookServiceImpl.getListBookForCustomer("Search", "Sort", "JaneDoe", null, 1, 3));
        verify(bookRepository).searchBooksByAuthorAndTitle((List<Author>) any(), (String) any(), (Pageable) any());
        verify(authorRepository).findByNameContainingIgnoreCase((String) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#getBookById(UUID)}
     */
    @Test
    void canGetBookById() {
        when(bookRepository.findById((UUID) any())).thenReturn(Optional.of(new Book()));
        BookDto actualBookById = bookServiceImpl.getBookById(UUID.randomUUID());
        assertNull(actualBookById.getTitle());
        assertNull(actualBookById.getThumbnailUrl());
        assertNull(actualBookById.getStatus());
        assertNull(actualBookById.getSmallThumbnailUrl());
        assertNull(actualBookById.getPublisher());
        assertNull(actualBookById.getPublishedDate());
        assertNull(actualBookById.getPageCount());
        assertNull(actualBookById.getLanguage());
        assertNull(actualBookById.getId());
        assertNull(actualBookById.getExternalId());
        assertNull(actualBookById.getDescription());
        verify(bookRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#createNewBook(GoogleBook)}
     */
    @Test
    void canCreateNewBook11() {
        Book book = new Book();
        when(bookRepository.save((Book) any())).thenReturn(book);
        when(categoryRepository.save((Category) any())).thenReturn(new Category());
        when(categoryRepository.findByName((String) any())).thenReturn(Optional.of(new Category()));
        when(authorRepository.save((Author) any())).thenReturn(new Author());
        when(authorRepository.findByName((String) any())).thenReturn(Optional.of(new Author()));

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("foo");

        ArrayList<String> stringList1 = new ArrayList<>();
        stringList1.add("foo");

        VolumeInfo volumeInfo = new VolumeInfo();
        volumeInfo.setImageLinks(new ImageLinks("Small Thumbnail", "Thumbnail"));
        volumeInfo.setAuthors(stringList1);
        volumeInfo.setCategories(stringList);

        GoogleBook googleBook = new GoogleBook();
        googleBook.setVolumeInfo(volumeInfo);
        assertSame(book, bookServiceImpl.createNewBook(googleBook));
        verify(bookRepository).save((Book) any());
        verify(categoryRepository).findByName((String) any());
        verify(authorRepository).findByName((String) any());
    }

    /**
     * Method under test: {@link BookServiceImpl#createNewBook(GoogleBook)}
     */
    @Test
    void canCreateNewBook12() {
        when(bookRepository.save((Book) any())).thenThrow(new EntityNotFoundException("An error occurred"));
        when(categoryRepository.save((Category) any())).thenReturn(new Category());
        when(categoryRepository.findByName((String) any())).thenReturn(Optional.of(new Category()));
        when(authorRepository.save((Author) any())).thenReturn(new Author());
        when(authorRepository.findByName((String) any())).thenReturn(Optional.of(new Author()));

        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("foo");

        ArrayList<String> stringList1 = new ArrayList<>();
        stringList1.add("foo");

        VolumeInfo volumeInfo = new VolumeInfo();
        volumeInfo.setImageLinks(new ImageLinks("Small Thumbnail", "Thumbnail"));
        volumeInfo.setAuthors(stringList1);
        volumeInfo.setCategories(stringList);

        GoogleBook googleBook = new GoogleBook();
        googleBook.setVolumeInfo(volumeInfo);
        assertThrows(EntityNotFoundException.class, () -> bookServiceImpl.createNewBook(googleBook));
        verify(bookRepository).save((Book) any());
        verify(categoryRepository).findByName((String) any());
        verify(authorRepository).findByName((String) any());
    }
}

