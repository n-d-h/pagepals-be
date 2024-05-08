package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.interview.InterviewDto;
import com.pagepal.capstone.dtos.request.RequestDto;
import com.pagepal.capstone.dtos.workingtime.WorkingTimeListRead;
import com.pagepal.capstone.enums.InterviewResultEnum;
import com.pagepal.capstone.enums.InterviewStateEnum;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

public interface RequestService {
    List<RequestDto> getListRequest();

    RequestDto getRequestById(UUID requestId);

    RequestDto updateRequestInterview(UUID staffId, UUID requestId, String interviewAt, String description) throws ParseException;

    RequestDto rejectRequest(UUID staffId, UUID requestId, String description);

    RequestDto acceptRequest(UUID staffId, UUID requestId, String description);

    RequestDto getRequestByReaderId(UUID readerId);

    RequestDto updateRequestToScheduling(UUID staffId, UUID requestId, String description);

    WorkingTimeListRead getWorkingTimeListByStaff(UUID staffId);

    InterviewDto updateInterviewTime(UUID requestId, String interviewAt) throws ParseException;

    List<RequestDto> getListRequestByReaderId(UUID readerId);

    InterviewDto updateInterview(UUID interviewId, InterviewStateEnum interviewStateEnum, InterviewResultEnum result, String note);
}
