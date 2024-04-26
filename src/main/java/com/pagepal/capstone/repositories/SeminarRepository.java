package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Seminar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface SeminarRepository extends JpaRepository<Seminar, UUID> {
    Page<Seminar> findAll(Pageable pageable);
    Page<Seminar> findAllByReaderId(UUID readerId, Pageable pageable);

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
        )
    """)
    Page<Seminar> findAllByCustomerIdNotJoin(UUID customerId, Pageable pageable);
}
