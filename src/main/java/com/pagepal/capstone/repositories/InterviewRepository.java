package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Interview;
import com.pagepal.capstone.enums.InterviewStateEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, UUID> {

    @Query("""
            SELECT i
            FROM Interview i
            WHERE i.request.staffId = :staffId
            AND i.state = :state
        """)
    List<Interview> findByStaffIdAndState(UUID staffId, InterviewStateEnum state);

}
