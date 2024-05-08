package com.pagepal.capstone.dtos.seminar;

import com.pagepal.capstone.dtos.booking.BookingDto;
import com.pagepal.capstone.dtos.event.EventDto;
import com.pagepal.capstone.entities.postgre.Book;
import com.pagepal.capstone.entities.postgre.Meeting;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.enums.SeminarStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeminarDto {
    private UUID id;
    private String title;
    private String description;
    private String imageUrl;
    private String rejectReason;
    private Integer duration;
    private String state;
    private String createdAt;
    private String updatedAt;
    private Reader reader;
    private Book book;
    private List<EventDto> events;
}
