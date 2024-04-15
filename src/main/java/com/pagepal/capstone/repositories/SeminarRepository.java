package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Seminar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SeminarRepository extends JpaRepository<Seminar, UUID> {
    Page<Seminar> findAll(Pageable pageable);
    Page<Seminar> findAllByReaderId(UUID readerId, Pageable pageable);
}
