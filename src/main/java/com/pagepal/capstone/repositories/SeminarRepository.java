package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.Seminar;
import com.pagepal.capstone.enums.SeminarStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface SeminarRepository extends JpaRepository<Seminar, UUID> {
    Page<Seminar> findAllByStatus(SeminarStatus status, Pageable pageable);
    
    Page<Seminar> findAllByReaderIdAndStatus(UUID readerId, SeminarStatus status, Pageable pageable);

    Page<Seminar> findAllByReaderId(UUID readerId, Pageable pageable);
    @Query("SELECT s FROM Seminar s" +
            " WHERE s.reader = :reader" +
            " AND EXTRACT(DAY FROM s.startTime) = EXTRACT(DAY FROM CAST(:startDate AS timestamp))" +
            " AND EXTRACT(MONTH FROM s.startTime) = EXTRACT(MONTH FROM CAST(:startDate AS timestamp))" +
            " AND EXTRACT(YEAR FROM s.startTime) = EXTRACT(YEAR FROM CAST(:startDate AS timestamp))")
    List<Seminar> findByReaderAndStartDate(Reader reader, Date startDate);

    List<Seminar> findByReaderAndStartTimeAfterOrderByStartTimeAsc(Reader reader, Date startDate);


    @Query("""
        SELECT s FROM Seminar s
        JOIN s.bookings b
        WHERE b.customer.id = :customerId
    """)
    Page<Seminar> findAllByCustomerId(UUID customerId, Pageable pageable);

    @Query("""
        SELECT s FROM Seminar s
        WHERE s.id NOT IN (
            SELECT s.id FROM Seminar s
            JOIN s.bookings b
            WHERE b.customer.id = :customerId
            AND b.state.name = 'PENDING'
        )
        AND s.status = :state
    """)
    Page<Seminar> findAllByCustomerIdNotJoin(UUID customerId, SeminarStatus state, Pageable pageable);
}
