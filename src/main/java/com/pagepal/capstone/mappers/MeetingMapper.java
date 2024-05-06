package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.meeting.MeetingDto;
import com.pagepal.capstone.entities.postgre.Meeting;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MeetingMapper {
    MeetingMapper INSTANCE = Mappers.getMapper(MeetingMapper.class);

    @Mapping(target = "timelines", ignore = true)
    MeetingDto toDto(Meeting meeting);

}
