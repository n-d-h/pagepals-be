package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.meeting.MeetingDto;
import com.pagepal.capstone.dtos.meeting.MeetingTimelineDto;
import com.pagepal.capstone.dtos.meeting.TimeLineCreateDto;
import com.pagepal.capstone.dtos.recording.MeetingRecordings;
import com.pagepal.capstone.dtos.recording.RecordingDto;
import com.pagepal.capstone.dtos.zoom.AuthZoomResponseDto;
import com.pagepal.capstone.services.MeetingService;
import com.pagepal.capstone.services.ZoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.text.ParseException;

@Controller
@RequiredArgsConstructor
public class MeetingController {

    private final ZoomService zoomService;
    private final MeetingService meetingService;

    @QueryMapping
    public RecordingDto getRecording(@Argument("roomId") String id){
        return zoomService.getRecording(id);
    }

    @QueryMapping
    public MeetingRecordings getMeetingRecordings(@Argument("meetingId") String id){
        return zoomService.getListRecordingByMeetingId(id);
    }

    @QueryMapping
    public MeetingDto getMeetingById(@Argument("roomId") String id){
        return meetingService.getMeetingById(id);
    }

    @QueryMapping
    public AuthZoomResponseDto getAuthZoom(){
        return zoomService.getZoomToken();
    }

    @MutationMapping
    public Boolean sendStatusChange(@Argument("userName") String userName, @Argument("event") String event){
        meetingService.sendStatusChange(userName, event);
        return true;
    }

    @MutationMapping
    public MeetingTimelineDto createTimeline(@Argument("input") TimeLineCreateDto input) throws ParseException {
        return meetingService.createTimeLine(input);
    }
}
