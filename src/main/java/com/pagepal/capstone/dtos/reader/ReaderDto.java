package com.pagepal.capstone.dtos.reader;

import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReaderDto {

    private UUID id;

    private String nickname;

    private int rating;

    private String genre;

    private String language;

    private String countryAccent;

    private String audioDescriptionUrl;

    private String description;

    private String totalOfReviews;

    private String totalOfBookings;

    private String introductionVideoUrl;

    private Double experience;

    private String tags;

    private Date createdAt = new Date();

    private Date updatedAt = new Date();

    private Date deletedAt;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private Account account;

    private Level level;

    private List<Service> services;

    private List<Follow> follows;

    private List<Promotion> promotions;

}
