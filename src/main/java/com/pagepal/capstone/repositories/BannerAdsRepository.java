package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.BannerAds;
import com.pagepal.capstone.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BannerAdsRepository extends JpaRepository<BannerAds, UUID> {
	List<BannerAds> findAllByStatus(Status status);
}
