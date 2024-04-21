package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.WithdrawRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WithdrawRequestRepository extends JpaRepository<WithdrawRequest, UUID>{

    @Query("""
            SELECT wr
            FROM WithdrawRequest wr
            WHERE wr.reader.id = :readerId
            """)
    Page<WithdrawRequest> findByReaderId(UUID readerId, Pageable pageable);
}
