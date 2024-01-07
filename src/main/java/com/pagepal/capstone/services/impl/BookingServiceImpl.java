package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.entities.postgre.Booking;
import com.pagepal.capstone.repositories.postgre.BookingRepository;
import com.pagepal.capstone.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> getAllBookingsByReaderId(Long readerId) {
        return null;
    }
}
