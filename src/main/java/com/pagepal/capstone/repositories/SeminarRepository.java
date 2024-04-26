package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.Seminar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface SeminarRepository extends JpaRepository<Seminar, UUID> {
    Page<Seminar> findAll(Pageable pageable);
    Page<Seminar> findAllByReaderId(UUID readerId, Pageable pageable);
    @Query("SELECT s FROM Seminar s" +
            " WHERE s.reader = :reader" +
            " AND EXTRACT(DAY FROM s.startTime) = EXTRACT(DAY FROM CAST(:startDate AS timestamp))" +
            " AND EXTRACT(MONTH FROM s.startTime) = EXTRACT(MONTH FROM CAST(:startDate AS timestamp))" +
            " AND EXTRACT(YEAR FROM s.startTime) = EXTRACT(YEAR FROM CAST(:startDate AS timestamp))")
    List<Seminar> findByReaderAndStartDate(Reader reader, Date startDate);

}
