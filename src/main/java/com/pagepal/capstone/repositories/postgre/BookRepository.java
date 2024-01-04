package com.pagepal.capstone.repositories.postgre;

import com.pagepal.capstone.entities.postgre.Book;
import com.pagepal.capstone.entities.postgre.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    // get list book by title and author
    Page<Book> findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(String title, String author, Pageable pageable);

    // get list book by title and author and Category
    Page<Book> findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCaseAndCategory(String title, String author, Category category, Pageable pageable);
}
