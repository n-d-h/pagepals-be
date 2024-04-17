package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Request;
import com.pagepal.capstone.enums.RequestStateEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, UUID>{

    @Query("""
            SELECT r
            FROM Request r
            WHERE r.reader.id = :readerId
            AND r.state IN :state
            """)
    Optional<Request> findByReaderIdAndStates(UUID readerId, List<RequestStateEnum> state);
}
