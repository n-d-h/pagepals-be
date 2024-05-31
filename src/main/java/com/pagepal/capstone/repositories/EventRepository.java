package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Event;
import com.pagepal.capstone.enums.EventStateEnum;
import com.pagepal.capstone.enums.SeminarStatus;
import com.pagepal.capstone.enums.Status;
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

    List<Event> findByStartAtBetweenAndState(Date startAt, Date endAt, EventStateEnum state);

    //    @Query(value = """
//            SELECT COUNT(e.id)
//            FROM event e
//            JOIN seminar s ON e.seminar_id = s.id
//            WHERE e.state = :state
//            AND s.state = :status
//            AND s.status = :state
//            AND e.start_at <= :end
//            AND s.reader_id = :readerId
//            AND (e.start_at + interval '1 minute' * s.duration) >= :start
//            """, nativeQuery = true)
//    long countConflictingEvents(@Param("readerId") UUID readerId,
//                                @Param("start") Date start,
//                                @Param("end") Date end,
//                                @Param("status") String status,
//                                @Param("state") String state);
    @Query("""
            SELECT COUNT(e.id)
            FROM Event e
            WHERE e.state = :eventState
            AND e.seminar.state = :seminarState
            AND e.seminar.status = :seminarStatus
            AND e.seminar.reader.id = :readerId
            AND e.endAt >= :start
            AND e.startAt <= :end
            """)
    long countConflictingEvents(EventStateEnum eventState, SeminarStatus seminarState, Status seminarStatus, UUID readerId, Date start, Date end);


//    @Query(value = """
//            SELECT COUNT(e.id)
//            FROM event e
//            JOIN seminar s ON e.seminar_id = s.id
//            WHERE e.state = :state
//            AND s.state = :status
//            AND s.status = :state
//            AND e.start_at <= :end
//            AND s.reader_id = :readerId
//            AND (e.start_at + interval '1 minute' * s.duration) >= :start
//            AND e.id != :eventId
//            """, nativeQuery = true)
//    long countConflictingEventsExceptEvent(@Param("readerId") UUID readerId,
//                                           @Param("start") Date start,
//                                           @Param("end") Date end,
//                                           @Param("status") String status,
//                                           @Param("state") String state,
//                                           @Param("eventId") UUID eventId);

    @Query("""
            SELECT COUNT(e.id)
            FROM Event e
            WHERE e.state = :eventState
            AND e.seminar.state = :seminarState
            AND e.seminar.status = :seminarStatus
            AND e.seminar.reader.id = :readerId
            AND e.endAt >= :start
            AND e.startAt <= :end
            AND e.id != :eventId
            """)
    long countConflictingEventsExceptEvent(EventStateEnum eventState, SeminarStatus seminarState, Status seminarStatus, UUID readerId, Date start, Date end, UUID eventId);


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
                AND e.endAt < :currentTime
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
    Page<Event> findAllEventCompletedByReaderId(UUID readerId, Date currentTime, Pageable pageable);

    @Query("""
                SELECT e
                FROM Event e
                WHERE e.seminar.reader.id = :readerId
                AND e.endAt < :currentTime
                AND EXISTS (
                    SELECT 1
                    FROM Booking b
                    WHERE b.event = e
                    AND b.state.name = 'CANCEL'
                )
            """)
    Page<Event> findAllEventCanceledByReaderId(UUID readerId, Date currentTime, Pageable pageable);

    @Query("""
                SELECT e
                FROM Event e
                WHERE e.seminar.reader.id = :readerId
                AND e.endAt < :currentTime
                AND EXISTS (
                    SELECT 1
                    FROM Booking b
                    WHERE b.event = e
                    AND b.state.name = 'PENDING'
                )
            """)
    Page<Event> findAllEventProcessingByReaderId(UUID readerId, Date currentTime, Pageable pageable);

    @Query("""
            SELECT e
            FROM Event e
            WHERE e.seminar.reader.id = :readerId
            AND e.state = :state
            AND e.endAt > :currentTime
            """)
    Page<Event> findAllEventActiveByReaderId(UUID readerId, EventStateEnum state, Date currentTime, Pageable pageable);

    @Query("""
            SELECT COUNT(e)
            FROM Event e
            WHERE e.seminar.reader.id = :readerId
            AND e.state = :state
            AND e.endAt > :currentTime
            """)
    Long countAllEventActiveByReaderId(UUID readerId, EventStateEnum state, Date currentTime);

    @Query("""
            SELECT e
            FROM Event e
            WHERE e.state = :state
            AND e.endAt > :startTime
            AND e.seminar.reader.account.accountState.name = 'READER_ACTIVE'
            AND NOT EXISTS (SELECT 1 FROM Booking b WHERE b.event.id = e.id AND b.customer.id = :customerId)
            """)
    Page<Event> findAllEventNotJoinByCustomer(UUID customerId, EventStateEnum state, Date startTime, Pageable pageable);

    @Query("""
            SELECT e
            FROM Event e
            WHERE e.state = :state
            AND e.endAt > :startAt
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

    @Query("""
            SELECT e
            FROM Event e
            WHERE e.state = :state
            AND e.endAt > :currentTime
            AND e.seminar.reader.account.accountState.name = 'READER_ACTIVE'
            ORDER BY e.startAt ASC, e.seminar.reader.rating DESC
            LIMIT 10
            """)
    List<Event> findTop10ActiveEventsOrderByStartAtAscAndReaderRatingDesc(EventStateEnum state, Date currentTime);


}
