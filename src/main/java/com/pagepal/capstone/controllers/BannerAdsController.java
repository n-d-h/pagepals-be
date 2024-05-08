package com.pagepal.capstone.controllers;

import java.util.List;

import com.pagepal.capstone.dtos.bannerads.BannerAdsDto;
import com.pagepal.capstone.services.BannerAdsService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BannerAdsController {
	private final BannerAdsService bannerAdsService;

	@QueryMapping("getAllBannerAds")
	public List<BannerAdsDto> getListBannerAds() {
		return bannerAdsService.getListBannerAds();
	}
}
