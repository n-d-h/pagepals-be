package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.WorkingTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface WorkingTimeRepository extends JpaRepository<WorkingTime, UUID> {

    List<WorkingTime> findByReaderAndDateOrderByStartTimeAsc(Reader reader, Date date);

    List<WorkingTime> findByReaderAndStartTimeAfterOrderByStartTimeAsc(Reader reader, Date startDate);

    List<WorkingTime> findByReaderAndStartTimeAfter(Reader reader, Date startDate);

    Boolean existsByStartTimeAndEndTimeAndReaderId(Date startTime, Date endTime, UUID readerId);


    // count the number of working times that overlap with the given time range
    long countByReaderAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(Reader reader, Date end, Date start);
}
