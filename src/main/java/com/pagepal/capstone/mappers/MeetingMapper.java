package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.meeting.MeetingDto;
import com.pagepal.capstone.entities.postgre.Meeting;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MeetingMapper {
    MeetingMapper INSTANCE = Mappers.getMapper(MeetingMapper.class);

    MeetingDto toDto(Meeting meeting);

}
