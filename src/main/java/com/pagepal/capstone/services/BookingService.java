package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.booking.BookingDto;
import com.pagepal.capstone.dtos.booking.QueryDto;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    List<BookingDto> getListBookingByReader(UUID readerId);
}
