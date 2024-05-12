package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Event;
import com.pagepal.capstone.enums.EventStateEnum;
import com.pagepal.capstone.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    @Query("""
            SELECT e
            FROM Event e
            WHERE e.seminar.reader.id = :readerId
            AND e.state = :state
            AND e.startAt > :startTime
            ORDER BY e.startAt ASC
            """)
    List<Event> findByReaderAndStatusAndStartTimeAfterOrderByStartTimeAsc(UUID readerId, EventStateEnum state, Date startTime);

    List<Event> findBySeminarId(UUID seminarId);

    Page<Event> findByStateAndStartAtAfter(EventStateEnum state, Date startAt, Pageable pageable);

    Page<Event> findBySeminarId(UUID seminarId, Pageable pageable);

    @Query("""
            SELECT e
            FROM Event e
            WHERE e.seminar.reader.id = :readerId
            """)
    Page<Event> findAllByReaderId(UUID readerId, Pageable pageable);

    @Query("""
            SELECT e
            FROM Event e
            WHERE e.seminar.reader.id = :readerId
            AND e.state = :state
            AND e.startAt > :startTime
            """)
    Page<Event> findAllEventActiveByReaderId(UUID readerId, EventStateEnum state, Date startTime, Pageable pageable);

    @Query("""
            SELECT e
            FROM Event e
            WHERE e.state = :state
            AND e.startAt > :startTime
            AND NOT EXISTS (SELECT 1 FROM Booking b WHERE b.event = e AND b.customer.id = :customerId)
            """)
    Page<Event> findAllEventNotJoinByCustomer(UUID customerId, EventStateEnum state, Date startTime, Pageable pageable);

    @Query("""
            SELECT e
            FROM Event e
            WHERE e.state = :state
            AND e.startAt > :startAt
            """)
    Page<Event> findAllActiveEvent(EventStateEnum state, Date startAt, Pageable pageable);
}
