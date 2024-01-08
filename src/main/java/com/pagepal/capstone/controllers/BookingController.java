package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.booking.BookingDto;
import com.pagepal.capstone.dtos.booking.QueryDto;
import com.pagepal.capstone.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @QueryMapping(name = "getListBookingByReader")
    public List<BookingDto> getListBookingByReader(
            @Argument(name = "readerId") String readerId
    ) {
        return bookingService.getListBookingByReader(UUID.fromString(readerId));
    }
}
