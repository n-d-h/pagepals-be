package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.request.RequestDto;
import com.pagepal.capstone.entities.postgre.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper
public interface RequestMapper {

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "toDateFormat")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "toDateFormat")
    @Mapping(target = "interviewAt", source = "interviewAt", qualifiedByName = "toDateFormat")
    RequestDto toDto(Request request);

    @Named("toDateFormat")
    default String toDateFormat(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return outputFormat.format(date);
    }
}
