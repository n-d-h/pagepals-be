package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Booking;
import com.pagepal.capstone.entities.postgre.BookingState;
import com.pagepal.capstone.entities.postgre.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.workingTime.reader.id = :readerId
            AND b.workingTime.reader.status = 'ACTIVE'
            """)
    Page<Booking> findAllByReaderId(UUID readerId, Pageable pageable);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.workingTime.reader.id = :readerId
            AND b.workingTime.reader.status = 'ACTIVE'
            AND b.service IS NOT NULL
            """)
    Page<Booking> findAllByReaderIdAndServiceIsNotNull(UUID readerId, Pageable pageable);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.workingTime.reader.id = :readerId
            AND b.workingTime.reader.status = 'ACTIVE'
            AND b.state.name = :state
            """)
    Page<Booking> findAllByReaderIdAndBookingState(UUID readerId, String state, Pageable pageable);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.workingTime.reader.id = :readerId
            AND b.workingTime.reader.status = 'ACTIVE'
            AND b.state.name = :state
            AND b.service IS NOT NULL
            """)
    Page<Booking> findAllByReaderIdAndBookingStateAndServiceIsNotNull(UUID readerId, String state, Pageable pageable);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.customer.id = :cusId
            AND b.customer.status = 'ACTIVE'
            AND b.state.name = :state
            """)
    Page<Booking> findAllByCustomerIdAndBookingState(UUID cusId, String state, Pageable pageable);

    Page<Booking> findByCustomer(Customer customer, Pageable pageable);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.state.name = :state
            """)
    List<Booking> findByStateString(String state);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.workingTime.reader.id = :readerId
            AND b.rating IS NOT NULL
            AND b.state.name = :state
            """)
    Page<Booking> findByRatingIsNotNullAndStateString(String state, UUID readerId, Pageable pageable);

    @Query("""
            SELECT COUNT (b) FROM Booking b
            WHERE b.service.id = :serviceId
            AND b.state.name = 'PENDING'
            """)
    Long countPendingBookingByService(UUID serviceId);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.service.id = :serviceId
            AND b.state.name = 'PENDING'
            """)
    List<Booking> findPendingBookingByService(UUID serviceId);

    @Query("""
            SELECT COUNT (b) FROM Booking b
            WHERE b.service.id = :serviceId
            AND (b.state.name = 'PENDING' OR b.state.name = 'COMPLETED' OR b.state.name = 'CANCEL')
            """)
    Long countBookingByService(UUID serviceId);

    @Query("""
            SELECT COUNT (b) FROM Booking b
            WHERE b.service.id = :serviceId
            AND (b.state.name = 'COMPLETED' OR b.state.name = 'CANCEL')
            """)
    Long countStateBookingByService(UUID serviceId);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.createAt BETWEEN :start AND :end
            AND b.service.id IS NOT NULL
            AND b.state.name = :state
            """)
    List<Booking> findByCreateAtBetweenAndStateAndServiceNotNull(Date start, Date end, String state);
}
