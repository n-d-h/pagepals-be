package com.pagepal.capstone.services;

import com.pagepal.capstone.entities.postgre.Booking;

import java.util.List;

public interface BookingService {
    List<Booking> getAllBookingsByReaderId(Long readerId);
}
