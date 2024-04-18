package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.meeting.MeetingDto;
import com.pagepal.capstone.dtos.recording.RecordingDto;
import com.pagepal.capstone.repositories.MeetingRepository;
import com.pagepal.capstone.services.MeetingService;
import com.pagepal.capstone.services.ZoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MeetingController {

    private final ZoomService zoomService;
    private final MeetingService meetingService;

    @QueryMapping
    public RecordingDto getRecording(@Argument("roomId") String id){
        return zoomService.getRecording(id);
    }

    public MeetingDto getMeetingById(@Argument("roomId") String id){
        return meetingService.getMeetingById(id);
    }
}
