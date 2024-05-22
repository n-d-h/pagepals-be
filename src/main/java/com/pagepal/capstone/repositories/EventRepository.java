package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Event;
import com.pagepal.capstone.enums.EventStateEnum;
import com.pagepal.capstone.enums.SeminarStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
            SELECT e
            FROM Event e
            WHERE e.seminar.id = :seminarId
            """)
    List<Event> findBySeminarId(UUID seminarId);

//    @Query("""
//            SELECT COUNT(e)
//            FROM Event e
//            JOIN e.seminar s
//            WHERE e.state = :state
//            AND s.state = :status
//            AND s.status = :state
//            AND e.startAt <= :end
//            AND s.reader.id = :readerId
//            AND (e.startAt + interval s.duration MINUTE) >= :start
//            """)
//    long countConflictingEvents(UUID readerId, Date start, Date end, SeminarStatus status, EventStateEnum state);


    @Query(value = """
            SELECT COUNT(e.id)
            FROM event e
            JOIN seminar s ON e.seminar_id = s.id
            WHERE e.state = :state
            AND s.state = :status
            AND s.status = :state
            AND e.start_at <= :end
            AND s.reader_id = :readerId
            AND (e.start_at + interval '1 minute' * s.duration) >= :start
            """, nativeQuery = true)
    long countConflictingEvents(@Param("readerId") UUID readerId,
                                @Param("start") Date start,
                                @Param("end") Date end,
                                @Param("status") String status,
                                @Param("state") String state);


    Page<Event> findByStateAndStartAtAfter(EventStateEnum state, Date startAt, Pageable pageable);

    @Query("""
            SELECT e
            FROM Event e
            WHERE e.seminar.id = :seminarId
            AND e.state = :state
            """)
    Page<Event> findBySeminarId(UUID seminarId, EventStateEnum state, Pageable pageable);

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
                AND e.startAt < :currentTime
                AND (EXISTS (
                        SELECT 1
                        FROM Booking b
                        WHERE b.event = e
                        AND b.state.name = 'COMPLETE'
                    )
                    OR NOT EXISTS (
                        SELECT 1
                        FROM Booking b
                        WHERE b.event = e
                    )
                )
            """)
    Page<Event> findAllEventCompletedByReaderId(UUID readerId, EventStateEnum state, Date currentTime, Pageable pageable);


    @Query("""
                SELECT e
                FROM Event e
                WHERE e.seminar.reader.id = :readerId
                AND e.state = :state
                AND e.startAt < :currentTime
                AND EXISTS (
                    SELECT 1
                    FROM Booking b
                    WHERE b.event = e
                    AND b.state.name = 'PENDING'
                )
            """)
    Page<Event> findAllEventProcessingByReaderId(UUID readerId, EventStateEnum state, Date currentTime, Pageable pageable);

    @Query("""
            SELECT e
            FROM Event e
            WHERE e.seminar.reader.id = :readerId
            AND e.state = :state
            AND e.startAt > :currentTime
            """)
    Page<Event> findAllEventActiveByReaderId(UUID readerId, EventStateEnum state, Date currentTime, Pageable pageable);

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

    @Query("""
            SELECT COUNT(e) FROM Event e
            JOIN e.seminar s
            WHERE s.reader.id = :readerId
            AND e.state = :state
            AND (e.createdAt BETWEEN :startDate AND :endDate)
            """)
    Long countByCreatedAtBetweenAndState(Date startDate, Date endDate, UUID readerId, EventStateEnum state);

}
