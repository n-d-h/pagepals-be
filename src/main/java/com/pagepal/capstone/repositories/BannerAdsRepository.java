package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.BannerAds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BannerAdsRepository extends JpaRepository<BannerAds, UUID> {

}
