package com.pagepal.capstone.dtos.bookingdetail;

import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailCreateDto {
    private String description;
    private UUID chapterId;
}
