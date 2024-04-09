package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.request.RequestDto;
import com.pagepal.capstone.services.ReaderService;
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
}
