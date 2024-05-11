package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.meeting.MeetingDto;
import com.pagepal.capstone.dtos.recording.MeetingRecordings;
import com.pagepal.capstone.entities.postgre.Meeting;
import com.pagepal.capstone.services.ZoomService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public interface MeetingMapper {
    MeetingMapper INSTANCE = Mappers.getMapper(MeetingMapper.class);

    
    @Mapping(target = "recordings", ignore = true)
    @Mapping(target = "timelines", ignore = true)
    MeetingDto toDto(Meeting meeting);

}
