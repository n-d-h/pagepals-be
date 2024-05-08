package com.pagepal.capstone.dtos.bannerads;

import com.pagepal.capstone.entities.postgre.Event;
import com.pagepal.capstone.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerAdsDto {
    private UUID id;

    private String createdAt;

    private String startAt;

    private String endAt;

    private Status status;

    private Event event;
}
