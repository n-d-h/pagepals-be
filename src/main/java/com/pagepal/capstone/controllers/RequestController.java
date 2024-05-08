package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.interview.InterviewDto;
import com.pagepal.capstone.dtos.request.RequestDto;
import com.pagepal.capstone.dtos.workingtime.WorkingTimeListRead;
import com.pagepal.capstone.enums.InterviewResultEnum;
import com.pagepal.capstone.enums.InterviewStateEnum;
import com.pagepal.capstone.services.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @QueryMapping
    public List<RequestDto> getListRequest() {
        return requestService.getListRequest();
    }

    @QueryMapping
    public RequestDto getRequestById(@Argument("requestId") UUID requestId) {
        return requestService.getRequestById(requestId);
    }

    @MutationMapping
    public RequestDto updateRequestInterview(@Argument("staffId") UUID staffId,
                                             @Argument("requestId") UUID requestId,
                                             @Argument("interviewAt") String interviewAt,
                                             @Argument("description") String description) throws ParseException {
        return requestService.updateRequestInterview(staffId, requestId, interviewAt, description);
    }

    @MutationMapping
    public RequestDto rejectRequest(@Argument("staffId") UUID staffId,
                                    @Argument("requestId") UUID requestId,
                                    @Argument("description") String description) {
        return requestService.rejectRequest(staffId, requestId,description);
    }

    @MutationMapping
    public RequestDto acceptRequest(@Argument("staffId") UUID staffId,
                                    @Argument("requestId") UUID requestId,
                                    @Argument("description") String description) {
        return requestService.acceptRequest(staffId, requestId,description);
    }

    @QueryMapping
    public RequestDto getRequestByReaderId(@Argument("readerId") UUID readerId) {
        return requestService.getRequestByReaderId(readerId);
    }

    @MutationMapping
    public RequestDto updateRequestToScheduling(@Argument("staffId") UUID staffId,
                                                @Argument("requestId") UUID requestId,
                                                @Argument("description") String description) {
        return requestService.updateRequestToScheduling(staffId, requestId, description);
    }

    @QueryMapping
    public WorkingTimeListRead getWorkingTimeListByStaffId(@Argument("staffId") UUID staffId) {
        return requestService.getWorkingTimeListByStaff(staffId);
    }

    @MutationMapping
    public InterviewDto updateInterviewTime(@Argument("requestId") UUID requestId,
                                            @Argument("interviewAt") String interviewAt) throws ParseException {
        return requestService.updateInterviewTime(requestId, interviewAt);
    }

    @QueryMapping
    public List<RequestDto> getListRequestByReaderId(@Argument("readerId") UUID readerId) {
        return requestService.getListRequestByReaderId(readerId);
    }

    @MutationMapping
    public InterviewDto updateInterview(@Argument("interviewId") UUID interviewId,
                                        @Argument("state") InterviewStateEnum interviewStateEnum,
                                        @Argument("result") InterviewResultEnum result,
                                        @Argument("note") String note) {
        return requestService.updateInterview(interviewId, interviewStateEnum, result, note);
    }
}
