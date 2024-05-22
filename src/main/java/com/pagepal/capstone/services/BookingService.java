package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.booking.*;
import com.pagepal.capstone.dtos.event.EventDto;
import com.pagepal.capstone.entities.postgre.Booking;

import java.util.UUID;

public interface BookingService {
    ListBookingDto getListBookingByReader(UUID readerId, QueryDto queryDto);

    ListBookingDto getListEventBookingByReader(UUID readerId, QueryDto queryDto);

    ListBookingDto getListBookingByCustomer(UUID cusId, QueryDto queryDto);

    BookingDto createBooking(UUID cusId, BookingCreateDto bookingDto);

    BookingDto cancelBooking(UUID id , String reason);

    BookingDto completeBooking(UUID id);

    EventDto completeEventBooking(UUID id);

    BookingDto reviewBooking(UUID id, ReviewBooking review);

    BookingDto getBookingById(UUID id);

    Integer updateRecordByBookingId(UUID id);

    Booking updateBookingRecord(Booking booking);
}
