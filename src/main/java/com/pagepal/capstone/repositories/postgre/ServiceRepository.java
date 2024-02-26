package com.pagepal.capstone.repositories.postgre;

import com.pagepal.capstone.entities.postgre.Book;
import com.pagepal.capstone.entities.postgre.Chapter;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {
    Page<Service> findAllByReader(Reader reader, Pageable pageable);

    @Query("SELECT s FROM Service s WHERE s.reader = ?1 AND s.chapter.book.title LIKE %?2%")
    Page<Service> findAllByReaderAndBookTitleContainsIgnoreCase(Reader reader, String title, Pageable pageable);

    @Query("SELECT s FROM Service s WHERE s.chapter.book = ?1 AND s.chapter.book.title LIKE %?2%")
    Page<Service> findAllByBookId(Book book, String title, Pageable pageable);

    Optional<Service> findByChapter(Chapter chapter);
}
