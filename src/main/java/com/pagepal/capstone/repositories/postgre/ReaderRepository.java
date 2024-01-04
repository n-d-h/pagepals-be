package com.pagepal.capstone.repositories.postgre;

import com.pagepal.capstone.entities.postgre.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, UUID>{
}
