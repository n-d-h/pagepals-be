package com.pagepal.capstone.repositories.postgre;

import com.pagepal.capstone.entities.postgre.Author;
import com.pagepal.capstone.entities.postgre.Book;
import com.pagepal.capstone.entities.postgre.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    Optional<Book> findByExternalId(String externalId);

    @Query("""
        SELECT DISTINCT b
        FROM Book b
        JOIN b.authors a
        JOIN b.categories c
        WHERE LOWER(b.title) LIKE %:title%
        AND (:author IS NULL OR a IN :author)
        AND (:category IS NULL OR c IN :category)
        """)
    Page<Book> searchBooksByAuthorCategoryAndTitle(List<Author> author,
                                                   List<Category> category,
                                                   String title,
                                                   Pageable pageable);
    @Query("""
        SELECT DISTINCT b
        FROM Book b
        JOIN b.authors a
        JOIN b.categories c
        WHERE LOWER(b.title) LIKE %:title%
        AND (:author IS NULL OR a IN :author)
        """)
    Page<Book> searchBooksByAuthorAndTitle(List<Author> author, String title, Pageable pageable);

    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
