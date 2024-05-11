package com.pagepal.capstone.dtos.meeting;

import com.pagepal.capstone.dtos.recording.MeetingRecordings;
import com.pagepal.capstone.enums.MeetingEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingDto {
    private UUID id;

    private String meetingCode;

    private String password;

    private Date startAt;

    private Date createAt;

    private MeetingEnum state;

    private List<MeetingTimelineDto> timelines;

//    private MeetingRecordings recordings;

}
