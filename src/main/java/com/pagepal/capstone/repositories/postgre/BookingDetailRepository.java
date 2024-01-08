package com.pagepal.capstone.repositories.postgre;

import com.pagepal.capstone.entities.postgre.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, UUID> {
}