package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.booking.BookingDto;
import com.pagepal.capstone.entities.postgre.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    BookingDto toDto(Booking booking);

    Booking toEntity(BookingDto bookingDto);
}
