package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.meeting.MeetingDto;
import com.pagepal.capstone.dtos.meeting.MeetingTimelineDto;
import com.pagepal.capstone.dtos.meeting.TimeLineCreateDto;

import java.text.ParseException;

public interface  MeetingService {

    MeetingDto getMeetingById(String id);

    void sendStatusChange(String userName, String event);

    MeetingTimelineDto createTimeLine(TimeLineCreateDto tl) throws ParseException;
}
