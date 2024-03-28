package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.booking.BookingCreateDto;
import com.pagepal.capstone.dtos.booking.BookingDto;
import com.pagepal.capstone.dtos.booking.ListBookingDto;
import com.pagepal.capstone.dtos.booking.QueryDto;

import java.util.UUID;

public interface BookingService {
    ListBookingDto getListBookingByReader(UUID readerId, QueryDto queryDto);

    ListBookingDto getListBookingByCustomer(UUID cusId, QueryDto queryDto);

    BookingDto createBooking(UUID cusId, BookingCreateDto bookingDto);

    BookingDto cancelBooking(UUID id);

    BookingDto completeBooking(UUID id);

}
