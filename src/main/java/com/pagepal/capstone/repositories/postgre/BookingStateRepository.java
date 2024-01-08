package com.pagepal.capstone.repositories.postgre;

import com.pagepal.capstone.entities.postgre.BookingState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingStateRepository extends JpaRepository<BookingState, UUID> {
}
