package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.booking.BookingDto;
import com.pagepal.capstone.dtos.booking.QueryDto;
import com.pagepal.capstone.mappers.BookingMapper;
import com.pagepal.capstone.repositories.postgre.BookingRepository;
import com.pagepal.capstone.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public List<BookingDto> getListBookingByReader(UUID readerId) {
        var res =  bookingRepository.findAllByReaderId(readerId);
        return res.stream().map(BookingMapper.INSTANCE::toDto).toList();
    }
}
