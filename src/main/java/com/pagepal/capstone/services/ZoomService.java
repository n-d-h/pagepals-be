package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.recording.RecordingDto;
import com.pagepal.capstone.dtos.zoom.AuthZoomResponseDto;
import com.pagepal.capstone.dtos.zoom.ZoomPlan;
import com.pagepal.capstone.entities.postgre.Meeting;
import com.pagepal.capstone.entities.postgre.Reader;

import java.util.Date;

public interface ZoomService {

    AuthZoomResponseDto getZoomToken();
    Meeting createMeeting(Reader reader, String agenda, Integer duration, String topic, Date startTime);
    Meeting createInterviewMeeting(String agenda, Integer duration, String topic, Date startTime);

    RecordingDto getRecording(String meetingId);

    ZoomPlan getZoomPlan();
}
