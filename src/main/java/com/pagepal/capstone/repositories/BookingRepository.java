package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Booking;
import com.pagepal.capstone.entities.postgre.BookingState;
import com.pagepal.capstone.entities.postgre.Customer;
import com.pagepal.capstone.entities.postgre.Event;
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
            WHERE b.event.seminar.reader.id = :readerId
            AND b.event.seminar.reader.status = 'ACTIVE'
            AND b.service IS NULL
            AND b.event IS NOT NULL
            """)
    Page<Booking> findAllByReaderIdAndServiceIsNullAndEventIsNotNull(UUID readerId, Pageable pageable);

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
            WHERE b.event.seminar.reader.id = :readerId
            AND b.event.seminar.reader.status = 'ACTIVE'
            AND b.state.name = :state
            AND b.service IS NULL
            AND b.event IS NOT NULL
            """)
    Page<Booking> findAllByReaderIdAndBookingStateAndServiceIsNullAndEventIsNotNull(UUID readerId, String state, Pageable pageable);

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
            AND (b.state.name = 'PENDING' OR b.state.name = 'COMPLETE' OR b.state.name = 'CANCEL')
            """)
    Long countBookingByService(UUID serviceId);

    @Query("""
            SELECT COUNT (b) FROM Booking b
            WHERE b.service.id = :serviceId
            AND (b.state.name = 'COMPLETE' OR b.state.name = 'CANCEL')
            """)
    Long countStateBookingByService(UUID serviceId);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.createAt BETWEEN :start AND :end
            AND b.service.id IS NOT NULL
            AND b.state.name = :state
            """)
    List<Booking> findByCreateAtBetweenAndStateAndServiceNotNull(Date start, Date end, String state);

    @Query("""
            SELECT count(b) FROM Booking b
            WHERE b.createAt BETWEEN :start AND :end
            AND b.workingTime.reader.id = :readerId
            AND (b.state.name = 'COMPLETE' OR b.state.name = 'CANCEL')
            """)
    Long countByCreateAtBetweenAndReaderIdAndState(Date start, Date end, UUID readerId);


    @Query("""
            SELECT count(b) FROM Booking b
            WHERE b.createAt <= CURRENT_TIMESTAMP
            AND b.workingTime.reader.id = :readerId
            AND (b.state.name = 'COMPLETE' OR b.state.name = 'CANCEL')
            """)
    Long countByReaderIdAndState(UUID readerId);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.createAt BETWEEN :start AND :end
            AND b.workingTime.reader.id = :readerId
            AND b.state.name = :state
            """)
    List<Booking> findByCreateAtBetweenAndReaderIdAndState(Date start, Date end, UUID readerId, String state);

    @Query("""
            SELECT count(b) FROM Booking b
            WHERE b.createAt <= :date
            AND b.workingTime.reader.id = :readerId
            AND b.state.name = :state
            """)
    Long countByCreateAtBeforeAndReaderIdAndState(Date date, UUID readerId, String state);

    @Query("""
            SELECT SUM(b.totalPrice) FROM Booking b
            WHERE b.createAt BETWEEN :start AND :end
            AND b.workingTime.reader.id = :readerId
            AND b.state.name = 'COMPLETE'
            """)
    Long sumPriceByCreateAtBetweenAndReaderId(Date start, Date end, UUID readerId);

    @Query("""
            SELECT SUM(b.totalPrice) FROM Booking b
            WHERE b.createAt <= :now
            AND b.workingTime.reader.id = :readerId
            AND b.state.name = 'COMPLETE'
            """)
    Double sumPriceByReaderId(UUID readerId, Date now);

    List<Booking> findAllByEventId(UUID eventId);

    List<Booking> findByEventAndState(Event event, BookingState state);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.customer.id = :cusId
            AND b.state.name = 'PENDING'
            AND b.startAt < :currentTime
            """)
    Page<Booking> findByStateProcessingAndCustomerId(UUID cusId, Date currentTime, Pageable pageable);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.service IS NOT NULL
            AND b.state.name = 'PENDING'
            AND b.startAt < :currentTime
            AND b.service.reader.id = :readerId
            AND b.service.reader.status = 'ACTIVE'
            """)
    Page<Booking> findByServiceStateProcessingAndReaderId(UUID readerId, Date currentTime, Pageable pageable);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.state.name = 'PENDING'
            AND b.event IS NOT NULL
            AND b.service IS NULL
            AND b.startAt < :currentTime
            AND b.event.seminar.reader.id = :readerId
            AND b.event.seminar.reader.status = 'ACTIVE'
            """)
    Page<Booking> findByEventStateProcessingAndReaderId(UUID readerId, Date currentTime, Pageable pageable);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.customer.id = :cusId
            AND b.state.name = 'PENDING'
            AND b.startAt >= :currentTime
            """)
    Page<Booking> findByStatePendingAndCustomerId(UUID cusId, Date currentTime, Pageable pageable);

    List<Booking> findByStartAtBetween(Date start, Date end);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.service IS NOT NULL
            AND b.state.name = 'PENDING'
            AND b.startAt BETWEEN :start AND :end
            """)
    List<Booking> findByStartAtBetweenAndStatePending(Date start, Date end);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.service IS NULL
            AND b.state.name = 'PENDING'
            AND b.startAt BETWEEN :start AND :end
            """)
    List<Booking> findByStartAtBetweenAndStatePendingEvent(Date start, Date end);


    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.workingTime.reader.id = :readerId
            AND b.workingTime.reader.status = 'ACTIVE'
            AND b.state.name = 'PENDING'
            AND b.service IS NOT NULL
            AND b.startAt >= :currentTime
            """)
    Page<Booking> findAllByReaderIdAndBookingStatePendingAndServiceIsNotNull(UUID readerId, Date currentTime, Pageable pageable);

    Booking findFirstByEventOrderByCreateAtDesc(Event event);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.event.seminar.reader.id = :readerId
            AND b.event.seminar.reader.status = 'ACTIVE'
            AND b.state.name = 'PENDING'
            AND b.service IS NULL
            AND b.event IS NOT NULL
            AND b.startAt >= :currentTime
            """)
    Page<Booking> findAllByReaderIdAndBookingStatePendingAndServiceIsNullAndEventIsNotNull(UUID readerId, Date currentTime, Pageable pageable);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.workingTime.reader.id = :readerId
            AND b.workingTime.reader.status = 'ACTIVE'
            AND b.state.name = 'PENDING'
            AND b.service IS NOT NULL
            AND b.startAt < :currentTime
            """)
    Page<Booking> findAllByReaderIdAndBookingStateProcessingAndServiceIsNotNull(UUID readerId, Date currentTime, Pageable pageable);
}
