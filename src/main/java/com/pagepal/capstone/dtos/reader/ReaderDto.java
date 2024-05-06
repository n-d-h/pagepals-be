package com.pagepal.capstone.dtos.reader;

import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.Service;
import com.pagepal.capstone.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReaderDto {

    private UUID id;

    private String nickname;

    private Integer rating;

    private String genre;

    private String language;

    private String countryAccent;

    private String audioDescriptionUrl;

    private String avatarUrl;

    private String thumbnailUrl;

    private String description;

    private Integer totalOfReviews;

    private Integer totalOfBookings;

    private String introductionVideoUrl;

    private String createdAt;

    private String updatedAt ;

    private String deletedAt;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private Account account;

    private ReaderDto readerRequestReference;

    private List<Service> services;

}
