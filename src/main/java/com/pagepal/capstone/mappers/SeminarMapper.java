package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.seminar.SeminarDto;
import com.pagepal.capstone.entities.postgre.Seminar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper
public interface SeminarMapper {
    SeminarMapper INSTANCE = Mappers.getMapper(SeminarMapper.class);

    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "toDateFormat")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "toDateFormat")
    SeminarDto toDto(Seminar seminar);

    @Named("toDateFormat")
    default String toDateFormat(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return outputFormat.format(date);
    }
}
