package com.pagepal.capstone.mappers;

import com.pagepal.capstone.dtos.meeting.MeetingDto;
import com.pagepal.capstone.dtos.meeting.MeetingTimelineDto;
import com.pagepal.capstone.dtos.recording.MeetingRecordings;
import com.pagepal.capstone.entities.postgre.Meeting;
import com.pagepal.capstone.entities.postgre.MeetingTimeline;
import com.pagepal.capstone.services.ZoomService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper
public interface MeetingMapper {
    MeetingMapper INSTANCE = Mappers.getMapper(MeetingMapper.class);

    
    MeetingDto toDto(Meeting meeting);

    @Mapping(target = "time", source = "time", qualifiedByName = "toDateFormat")
    MeetingTimelineDto toTimeLineDto(MeetingTimeline meeting);

    @Named("toDateFormat")
    default String toDateFormat(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return outputFormat.format(date);
    }

}
