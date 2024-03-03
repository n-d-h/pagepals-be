package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.booking.BookingCreateDto;
import com.pagepal.capstone.dtos.booking.BookingDto;
import com.pagepal.capstone.dtos.booking.ListBookingDto;
import com.pagepal.capstone.dtos.booking.QueryDto;
import com.pagepal.capstone.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @QueryMapping(name = "getListBookingByReader")
    public ListBookingDto getListBookingByReader(
            @Argument(name = "readerId") String readerId,
            @Argument(name = "filter") QueryDto queryDto
    ) {
        return bookingService.getListBookingByReader(UUID.fromString(readerId), queryDto);
    }

    @QueryMapping
    public ListBookingDto getListBookingByCustomer(@Argument(name = "customerId") String id, @Argument(name = "filter") QueryDto queryDto) {
        return bookingService.getListBookingByCustomer(UUID.fromString(id), queryDto);
    }

    @MutationMapping
    public BookingDto createBooking(@Argument(name = "customerId") String id, @Argument(name = "booking") BookingCreateDto bookingDto) {
        return bookingService.createBooking(UUID.fromString(id), bookingDto);
    }
}
