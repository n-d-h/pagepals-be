package com.pagepal.capstone.services.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.pagepal.capstone.dtos.bannerads.BannerAdsDto;
import com.pagepal.capstone.entities.postgre.BannerAds;
import com.pagepal.capstone.entities.postgre.Event;
import com.pagepal.capstone.enums.EventStateEnum;
import com.pagepal.capstone.enums.SeminarStatus;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.BannerAdsRepository;
import com.pagepal.capstone.repositories.EventRepository;
import com.pagepal.capstone.services.BannerAdsService;
import com.pagepal.capstone.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BannerAdsServiceImpl implements BannerAdsService {

    private final BannerAdsRepository bannerAdsRepository;
    private final EventRepository eventRepository;
    private final DateUtils dateUtils;

    @Override
    public List<BannerAdsDto> getListBannerAds() {
        Date currentTime = dateUtils.getCurrentVietnamDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        Date modifiedTime = calendar.getTime();
        List<Event> events = eventRepository
                .findTop10ActiveEventsOrderByStartAtAscAndReaderRatingDesc(
                        EventStateEnum.ACTIVE, modifiedTime, EventStateEnum.ACTIVE, SeminarStatus.ACCEPTED);
        List<BannerAds> bannerAdsList = new ArrayList<>();
        for (Event event : events) {
            bannerAdsList.add(
                    BannerAds.builder()
                            .createdAt(event.getCreatedAt())
                            .startAt(modifiedTime)
                            .endAt(event.getStartAt())
                            .status(Status.ACTIVE)
                            .event(event)
                            .build());
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
