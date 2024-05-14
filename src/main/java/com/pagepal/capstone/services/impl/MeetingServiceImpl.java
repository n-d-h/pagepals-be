package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.meeting.MeetingDto;
import com.pagepal.capstone.dtos.meeting.MeetingTimelineDto;
import com.pagepal.capstone.dtos.meeting.TimeLineCreateDto;
import com.pagepal.capstone.entities.postgre.Meeting;
import com.pagepal.capstone.entities.postgre.MeetingTimeline;
import com.pagepal.capstone.enums.MeetingEnum;
import com.pagepal.capstone.mappers.MeetingMapper;
import com.pagepal.capstone.repositories.MeetingRepository;
import com.pagepal.capstone.repositories.MeetingTimelineRepository;
import com.pagepal.capstone.services.MeetingService;
import com.pagepal.capstone.services.WebhookService;
import com.pagepal.capstone.utils.DateUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final WebhookService webhookService;
    private final DateUtils dateUtils;
    private final MeetingTimelineRepository meetingTimelineRepository;

    @Override
    public MeetingDto getMeetingById(String id) {
        Meeting meeting = meetingRepository
                .findByMeetingCodeAndState(id, MeetingEnum.AVAILABLE)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));
        return MeetingMapper.INSTANCE.toDto(meeting);
    }

    @Override
    public void sendStatusChange(String userName, String event) {
        Map<String, String> content = new HashMap<>();
        content.put("Content", "User terminate meeting");
        content.put("Task executed at", dateUtils.getCurrentVietnamDate().toString());
        content.put("User name", userName);
        content.put("Event", event);
        webhookService.sendWebhookWithDataSchedule("User terminate meeting", content, Boolean.FALSE);
    }

    @Override
    public MeetingTimelineDto createTimeLine(TimeLineCreateDto tl) throws ParseException {

        //convert date string format yyyy-MM-dd HH:mm:ss to Date type
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse(tl.getTime());

        MeetingTimeline timeline = new MeetingTimeline();
        timeline.setUserName(tl.getUserName());
        timeline.setAction(tl.getAction());
        timeline.setTime(date);
        timeline.setMeeting(
                meetingRepository
                        .findByMeetingCodeAndState(tl.getMeetingCode(), MeetingEnum.AVAILABLE)
                        .orElseThrow(() -> new RuntimeException("Meeting not found")));

        timeline = meetingTimelineRepository.save(timeline);

        return MeetingMapper.INSTANCE.toTimeLineDto(timeline);
    }
}
