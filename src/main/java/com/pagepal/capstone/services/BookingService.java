package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.booking.*;

import java.util.UUID;

public interface BookingService {
    ListBookingDto getListBookingByReader(UUID readerId, QueryDto queryDto);

    ListBookingDto getListBookingByCustomer(UUID cusId, QueryDto queryDto);

    BookingDto createBooking(UUID cusId, BookingCreateDto bookingDto);

    BookingDto cancelBooking(UUID id);

    BookingDto completeBooking(UUID id);

    BookingDto reviewBooking(UUID id, ReviewBooking review);
}
