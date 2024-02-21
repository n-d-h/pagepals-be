package com.pagepal.capstone.repositories.postgre;


import com.pagepal.capstone.entities.postgre.Book;
import com.pagepal.capstone.entities.postgre.Category;
import com.pagepal.capstone.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration(classes = {BookRepository.class, CategoryRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.pagepal.capstone.entities.postgre"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    //Date
    LocalDate localDate1 = LocalDate.of(2023, 12, 21);
    LocalDate localDate2 = LocalDate.of(2022, 12, 21);
    LocalDate localDate3 = LocalDate.of(2021, 12, 21);
    Date date1 = java.sql.Date.valueOf(localDate1);
    Date date2 = java.sql.Date.valueOf(localDate2);
    Date date3 = java.sql.Date.valueOf(localDate3);

    //Category
    Category category1 = new Category(UUID.randomUUID(), "category1", "des1", Status.ACTIVE, null);
    Category category2 = new Category(UUID.randomUUID(), "category2", "des2", Status.ACTIVE, null);
    Category category3 = new Category(UUID.randomUUID(), "category3", "des3", Status.ACTIVE, null);

    //Book
    Book book1 = new Book(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "book1",
            "book1mock", "author1", "publisher1", 123L,
            "vietnamese", null, null, null,
            date1, Status.ACTIVE, category1, null);
    Book book2 = new Book(UUID.randomUUID(), "book2",
            "book2mock", "author2", "publisher2", 234L,
            "english", null, null, null,
            date2, Status.ACTIVE, category1, null);
    Book book3 = new Book(UUID.randomUUID(), "book3",
            "book3mock", "author3", "publisher3", 345L,
            "vietnamese", null, null, null,
            date3, Status.ACTIVE, category2, null);



    /**
     * Method under test: {@link BookRepository#findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(String, String, Pageable)}
     */
    @Test
    void canFindByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase() {
        // Arrange
        categoryRepository.saveAll(Arrays.asList(category1, category2, category3));
        bookRepository.saveAll(Arrays.asList(book1, book2, book3));

        // Act
        Page<Book> result = bookRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase("book", "author", PageRequest.of(0, 10));

        // Asserts
        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
    }

    @Test
    void shouldReturnEmptyListWhenNoBookFoundWithFindingByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase() {
        // Arrange
        categoryRepository.saveAll(Arrays.asList(category1, category2, category3));
        bookRepository.saveAll(Arrays.asList(book1, book2, book3));

        // Act
        Page<Book> result = bookRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase("book", "author4", PageRequest.of(0, 10));

        // Asserts
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
    }

    /**
     * Method under test: {@link BookRepository#findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndCategory(String, String, Category, Pageable)}
     */
    @Test
    void canFindByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndCategory() {
        // Arrange
        categoryRepository.saveAll(Arrays.asList(category1, category2, category3));
        bookRepository.saveAll(Arrays.asList(book1, book2, book3));

        // Act
        Page<Book> result = bookRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndCategory("book", "author", category1, PageRequest.of(0, 10));

        // Asserts
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
    }

    @Test
    void shouldReturnEmptyListWhenNoBookFoundWithFindingByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndCategory() {
        // Arrange
        categoryRepository.saveAll(Arrays.asList(category1, category2, category3));
        bookRepository.saveAll(Arrays.asList(book1, book2, book3));

        // Act
        Page<Book> result = bookRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndCategory("book", "author4", category3, PageRequest.of(0, 10));

        // Asserts
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
    }
}




