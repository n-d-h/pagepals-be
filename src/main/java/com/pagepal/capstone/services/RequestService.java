package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.request.RequestDto;

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
}
