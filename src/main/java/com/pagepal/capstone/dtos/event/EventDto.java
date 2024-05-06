package com.pagepal.capstone.dtos.event;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pagepal.capstone.dtos.bannerads.BannerAdsDto;
import com.pagepal.capstone.dtos.booking.BookingDto;
import com.pagepal.capstone.dtos.seminar.SeminarDto;
import com.pagepal.capstone.entities.postgre.BannerAds;
import com.pagepal.capstone.entities.postgre.Booking;
import com.pagepal.capstone.entities.postgre.Seminar;
import com.pagepal.capstone.enums.EventStateEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    private UUID id;

    private String startAt;

    private String createdAt;

    private Integer limitCustomer;

    private Integer activeSlot;

    private Boolean isFeatured;

    private EventStateEnum state;

    private SeminarDto seminar;

    private List<BookingDto> bookings;

    private List<BannerAdsDto> bannerAds;
}
