package com.pagepal.capstone.dtos.bookingdetail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailCreateDto {
    private String description;
    private UUID chapterId;
}
