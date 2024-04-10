package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.workingtime.WorkingTimeDto;
import com.pagepal.capstone.entities.postgre.WorkingTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface WorkingTimeMapper {
    WorkingTimeMapper INSTANCE = Mappers.getMapper(WorkingTimeMapper.class);

    WorkingTimeDto toDto(WorkingTime workingTime);

    WorkingTime toEntity(WorkingTimeDto workingTimeDto);

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
