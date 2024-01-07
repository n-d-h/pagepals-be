package com.pagepal.capstone.repositories.postgre;

import com.pagepal.capstone.entities.postgre.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Query("""
            SELECT b
            FROM Booking b
            JOIN FETCH b.meeting m
            JOIN FETCH m.reader r
            WHERE r.id = :readerId
            AND r.status = 'ACTIVE'
            AND b.
            """)
    Page<Booking> findAllByReaderId(Long readerId, Pageable pageable);
}
