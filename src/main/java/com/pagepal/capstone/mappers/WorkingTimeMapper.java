package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.workingtime.WorkingTimeDto;
import com.pagepal.capstone.entities.postgre.WorkingTime;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface WorkingTimeMapper {
    WorkingTimeMapper INSTANCE = Mappers.getMapper(WorkingTimeMapper.class);

    @Mapping(target = "isBooked", ignore = true)
    WorkingTimeDto toDto(WorkingTime workingTime);

    WorkingTime toEntity(WorkingTimeDto workingTimeDto);

    @AfterMapping
    default void setIsBookedToDto(WorkingTime workingTime, @MappingTarget WorkingTimeDto workingTimeDto) {
        Boolean isBooked = setIsBookedFunc(workingTime);
        workingTimeDto.setIsBooked(isBooked);
    }

    default Boolean setIsBookedFunc(WorkingTime workingTime) {
        if (workingTime.getBookings() != null && !workingTime.getBookings().isEmpty()) {
            for ( var booking : workingTime.getBookings()) {
                if (booking.getState().getName().equals("CANCEL")) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

//    @Mapping(target = "reader", source = "readerId", qualifiedByName = "readerIdToReader")
//    WorkingTime toEntity(WorkingTimeCreateDto workingTimeCreateDto);
//
//    @Named("readerIdToReader")
//    default Reader readerIdToReader(UUID id) {
//        Reader reader = new Reader();
//        reader.setId(id);
//        return reader;
//    }

    @Named("toDateFormat")
    default String toDateFormat(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return outputFormat.format(date);
    }
}
