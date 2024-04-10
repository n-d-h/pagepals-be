package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.booking.BookingDto;
import com.pagepal.capstone.entities.postgre.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "createAt", source = "createAt", qualifiedByName = "toDateFormat")
    @Mapping(target = "deleteAt", source = "deleteAt", qualifiedByName = "toDateFormat")
    @Mapping(target = "startAt", source = "startAt", qualifiedByName = "toDateFormat")
    @Mapping(target = "updateAt", source = "updateAt", qualifiedByName = "toDateFormat")
    BookingDto toDto(Booking booking);

    Booking toEntity(BookingDto bookingDto);

    @Named("toDateFormat")
    default String toDateFormat(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return outputFormat.format(date);
    }
}
