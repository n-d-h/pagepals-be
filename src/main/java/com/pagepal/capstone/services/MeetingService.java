package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.meeting.MeetingDto;

import java.util.UUID;

public interface MeetingService {

    MeetingDto getMeetingById(String id);
}
