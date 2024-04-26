package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Book;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.Service;
import com.pagepal.capstone.entities.postgre.ServiceType;
import com.pagepal.capstone.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {
    Page<Service> findAllByReader(Reader reader, Pageable pageable);

    @Query("SELECT s FROM Service s WHERE s.reader = ?1 AND LOWER(s.book.title) LIKE %?2% AND (s.isDeleted = false OR s.isDeleted IS NULL)")
    Page<Service> findAllByReaderAndBookTitleContainsIgnoreCase(Reader reader, String title, Pageable pageable);

    @Query("SELECT s FROM Service s WHERE s.book = ?1 AND s.book.title LIKE %?2% AND (s.isDeleted = false OR s.isDeleted IS NULL)")
    Page<Service> findAllByBookId(Book book, String title, Pageable pageable);

    long countByStatus(Status status);

    @Query("SELECT s FROM Service s WHERE s.serviceType = ?1 AND s.book= ?2 AND (s.isDeleted = false OR s.isDeleted IS NULL)")
    List<Service> findByServiceTypeAndBook(ServiceType serviceType, Book book);

    @Query("""
            SELECT s FROM Service s
            WHERE s.reader.id = :readerId
            AND (s.isDeleted IS NULL OR s.isDeleted = false)
            ORDER BY s.createdAt DESC
            """)
    Page<Service> findByReaderId(UUID readerId, Pageable pageable);

    @Query("""
            SELECT s FROM Service s
            WHERE s.reader.id = :readerId
            AND s.book.title LIKE %:title%
            AND (s.isDeleted IS NULL OR s.isDeleted = false)
            ORDER BY s.createdAt DESC
            """)
    Page<Service> findByReaderIdAndBookTitleContaining(UUID readerId, String title, Pageable pageable);
}
