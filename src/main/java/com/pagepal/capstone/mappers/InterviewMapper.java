package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.interview.InterviewDto;
import com.pagepal.capstone.entities.postgre.Interview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper
public interface InterviewMapper {

    InterviewMapper INSTANCE = Mappers.getMapper(InterviewMapper.class);

    @Mapping(target = "interviewAt", source = "interviewAt", qualifiedByName = "toDateFormat")
    InterviewDto toDto(Interview request);

    @Named("toDateFormat")
    default String toDateFormat(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return outputFormat.format(date);
    }
}
