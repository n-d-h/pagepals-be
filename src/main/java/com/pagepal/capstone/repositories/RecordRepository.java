package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecordRepository extends JpaRepository<Record, UUID> {

    Optional<Record> findByExternalId(String externalId);
}
