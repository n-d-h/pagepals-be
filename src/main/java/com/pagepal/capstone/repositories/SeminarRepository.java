package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.Seminar;
import com.pagepal.capstone.enums.SeminarStatus;
import com.pagepal.capstone.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface SeminarRepository extends JpaRepository<Seminar, UUID> {
    @Query(""" 
            SELECT COUNT(s) FROM Seminar s
            WHERE s.reader.id = :readerId
            AND s.createdAt
            BETWEEN :startDate
            AND :endDate
            AND s.status = 'ACTIVE'
            """
    )
    Long countByReaderIdAndCreatedAtBetween(UUID readerId, Date startDate, Date endDate);

    Page<Seminar> findByStatus(Status status, Pageable pageable);

    Page<Seminar> findByStateAndStatus(SeminarStatus state, Status status, Pageable pageable);

    Page<Seminar> findByStateAndStatusAndReader(SeminarStatus state, Status status, Reader reader, Pageable pageable);

    Page<Seminar> findByStatusAndReader(Status status, Reader reader, Pageable pageable);
}
