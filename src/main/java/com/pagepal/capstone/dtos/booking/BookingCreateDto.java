package com.pagepal.capstone.dtos.booking;

import com.pagepal.capstone.dtos.bookingdetail.BookingDetailCreateDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateDto {
    private Double totalPrice;
    private Date startAt;
    private String promotionCode;
    private String description;
    private List<BookingDetailCreateDto> bookingDetails;
}
