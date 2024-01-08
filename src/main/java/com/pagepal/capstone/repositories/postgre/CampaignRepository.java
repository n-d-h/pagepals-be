package com.pagepal.capstone.repositories.postgre;

import com.pagepal.capstone.entities.postgre.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, UUID> {
    @Query("""
            SELECT c
            FROM Campaign c
            WHERE c.title LIKE %:title%
            """)
    Page<Campaign> findAllCampaignWithSearch(String title, Pageable pageable);
}
