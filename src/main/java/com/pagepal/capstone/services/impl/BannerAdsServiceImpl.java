package com.pagepal.capstone.services.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.pagepal.capstone.dtos.bannerads.BannerAdsDto;
import com.pagepal.capstone.entities.postgre.BannerAds;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.BannerAdsRepository;
import com.pagepal.capstone.services.BannerAdsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BannerAdsServiceImpl implements BannerAdsService {

	private final BannerAdsRepository bannerAdsRepository;

	@Override
	public List<BannerAdsDto> getListBannerAds() {
		List<BannerAds> bannerAdsList = bannerAdsRepository.findAllByStatus(Status.ACTIVE);
		for(BannerAds bannerAds : bannerAdsList) {
			Date endAt = bannerAds.getEndAt();
			if(endAt.before(new Date())) {
				bannerAds.setStatus(Status.INACTIVE);
				bannerAdsRepository.save(bannerAds);
			}
		}

		return bannerAdsList.stream().map(this::convertToDto).toList();
	}

	private BannerAdsDto convertToDto(BannerAds bannerAds) {
		return BannerAdsDto.builder()
				.id(bannerAds.getId())
				.createdAt(bannerAds.getCreatedAt().toString())
				.startAt(bannerAds.getStartAt().toString())
				.endAt(bannerAds.getEndAt().toString())
				.status(bannerAds.getStatus())
				.event(bannerAds.getEvent())
				.build();
	}
}
