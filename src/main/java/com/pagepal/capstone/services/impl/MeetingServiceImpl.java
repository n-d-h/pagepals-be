package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.meeting.MeetingDto;
import com.pagepal.capstone.entities.postgre.Meeting;
import com.pagepal.capstone.enums.MeetingEnum;
import com.pagepal.capstone.mappers.MeetingMapper;
import com.pagepal.capstone.repositories.MeetingRepository;
import com.pagepal.capstone.services.MeetingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;

    @Override
    public MeetingDto getMeetingById(String id) {
        Meeting meeting = meetingRepository
                .findByMeetingCodeAndState(id, MeetingEnum.AVAILABLE)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));
        return MeetingMapper.INSTANCE.toDto(meeting);
    }
}
