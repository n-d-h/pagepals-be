package com.pagepal.capstone.dtos.booking;

import com.pagepal.capstone.dtos.bookingdetail.BookingDetailCreateDto;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateDto {
    private UUID workingTimeId;
    private String meetingCode;
    private Double totalPrice;
    private String promotionCode;
    private String description;
    private List<BookingDetailCreateDto> bookingDetails;
}
