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

    @Query("SELECT s FROM Service s WHERE s.reader = ?1 AND s.book = ?2 AND (s.isDeleted = false OR s.isDeleted IS NULL)")
    Page<Service> findAllByReaderAndBook(Reader reader, Book book, Pageable pageable);

    @Query("""
            SELECT s
            FROM Service s
            WHERE s.reader = ?1
            AND s.book = ?2
            AND LOWER(s.shortDescription) LIKE LOWER(CONCAT('%', ?3, '%'))
            AND (s.isDeleted = false OR s.isDeleted IS NULL)
            """)
    Page<Service> findAllByReaderAndBookAndDescriptionContainsIgnoreCase(Reader reader, Book book, String title, Pageable pageable);


    long countByStatus(Status status);

    @Query("SELECT s FROM Service s WHERE s.serviceType = ?1 AND s.book = ?2 AND s.reader = ?3 AND (s.isDeleted = false OR s.isDeleted IS NULL)")
    List<Service> findByServiceTypeAndBook(ServiceType serviceType, Book book, Reader reader);

    @Query("SELECT s FROM Service s WHERE s.reader = ?1 AND s.book = ?2 AND (s.isDeleted = false OR s.isDeleted IS NULL)")
    List<Service> findByReaderAndBook(Reader reader, Book book);

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
            AND LOWER(s.book.title) LIKE LOWER(CONCAT('%', :title, '%'))
            AND (s.isDeleted IS NULL OR s.isDeleted = false)
            ORDER BY s.createdAt DESC
            """)
    Page<Service> findByReaderIdAndBookTitleContaining(UUID readerId, String title, Pageable pageable);

    @Query("""
            SELECT COUNT(s)
            FROM Service s
            WHERE s.reader.id = :readerId
            AND (s.isDeleted IS NULL OR s.isDeleted = false)
            """)
    Long countActiveServicesByReaderId(UUID readerId);

    @Query("""
            SELECT COUNT(s)
            FROM Service s
            WHERE s.reader.id = :readerId
            AND s.book.id = :bookId
            AND (s.isDeleted IS NULL OR s.isDeleted = false)
            """)
    Integer countActiveServicesByReaderIdAndBookId(UUID readerId, UUID bookId);


    @Query("""
            SELECT MIN(s.price)
            FROM Service s
            WHERE s.reader.id = :readerId
            AND s.book.id = :bookId
            AND (s.isDeleted IS NULL OR s.isDeleted = false)
            """)
    Integer getMinPriceByReaderIdAndBookId(UUID readerId, UUID bookId);

    @Query("""
            SELECT MAX(s.price)
            FROM Service s
            WHERE s.reader.id = :readerId
            AND s.book.id = :bookId
            AND (s.isDeleted IS NULL OR s.isDeleted = false)
            """)
    Integer getMaxPriceByReaderIdAndBookId(UUID readerId, UUID bookId);

    // get all rating of a book service
    @Query("""
            SELECT s.rating
            FROM Service s
            WHERE s.book.id = :bookId
            AND s.reader.id = :readerId
            AND (s.rating IS NOT NULL AND s.rating > 0)
            AND (s.isDeleted IS NULL OR s.isDeleted = false)
            """)
    List<Integer> getAllRatingByReaderIdAndBookId(UUID readerId, UUID bookId);
}
