package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.seminar.ListSeminarDto;
import com.pagepal.capstone.dtos.seminar.SeminarCreateDto;
import com.pagepal.capstone.dtos.seminar.SeminarDto;
import com.pagepal.capstone.dtos.seminar.SeminarUpdateDto;
import com.pagepal.capstone.enums.SeminarStatus;
import com.pagepal.capstone.services.SeminarService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class SeminarController {
    private final SeminarService seminarService;

    @QueryMapping(name = "getAllSeminarRequests")
    public ListSeminarDto getAllSeminarRequests(
            @Argument("page") Integer page,
            @Argument("pageSize") Integer pageSize,
            @Argument("sort") String sort
    ) {
        return seminarService.getAllSeminarRequests(page, pageSize, sort);
    }

    @QueryMapping(name = "getAllSeminarRequestsByState")
    public ListSeminarDto getAllSeminarRequestsByState(
            @Argument("page") Integer page,
            @Argument("pageSize") Integer pageSize,
            @Argument("sort") String sort,
            @Argument("state") SeminarStatus state
    ) {
        return seminarService.getAllSeminarRequestsByState(page, pageSize, sort, state);
    }

    @QueryMapping(name = "getAllSeminarRequestsByReaderId")
    public ListSeminarDto getAllSeminarRequestsByReaderId(
            @Argument("readerId") UUID readerId,
            @Argument("page") Integer page,
            @Argument("pageSize") Integer pageSize,
            @Argument("sort") String sort
    ) {
        return seminarService.getAllSeminarRequestsByReaderId(readerId, page, pageSize, sort);
    }

    @QueryMapping(name = "getAllSeminarRequestsByReaderIdAndState")
    public ListSeminarDto getAllSeminarRequestsByReaderIdAndState(
            @Argument("readerId") UUID readerId,
            @Argument("page") Integer page,
            @Argument("pageSize") Integer pageSize,
            @Argument("sort") String sort,
            @Argument("state") SeminarStatus state,
            @Argument("search") String search
    ) {
        return seminarService.getAllSeminarRequestsByReaderIdAndState(readerId, page, pageSize, sort, state, search);
    }

    @QueryMapping(name = "getSeminarRequest")
    public SeminarDto getSeminarRequest(@Argument("id") UUID id) {
        return seminarService.getSeminarRequest(id);
    }

    @MutationMapping(name = "createSeminarRequest")
    public SeminarDto createSeminarRequest(
            @Argument("create") SeminarCreateDto seminarCreateDto) {
        return seminarService.createSeminarRequest(seminarCreateDto);
    }

    @MutationMapping(name = "updateSeminarRequest")
    public SeminarDto updateSeminarRequest(
            @Argument("update") SeminarUpdateDto seminarUpdateDto) {
        return seminarService.updateSeminarRequest(seminarUpdateDto);
    }

    @MutationMapping(name = "deleteSeminarRequest")
    public SeminarDto deleteSeminarRequest(
            @Argument("id") UUID id) {
        return seminarService.deleteSeminarRequest(id);
    }

    @MutationMapping(name = "acceptSeminarRequest")
    public SeminarDto acceptSeminarRequest(
            @Argument("requestId") UUID id,
            @Argument("staffId") UUID staffId) {
        return seminarService.updateSeminarRequestState(id, SeminarStatus.ACCEPTED, null, staffId);
    }

    @MutationMapping(name = "rejectSeminarRequest")
    public SeminarDto rejectSeminarRequest(
            @Argument("requestId") UUID id,
            @Argument("reason") String reason,
            @Argument("staffId") UUID staffId) {
        return seminarService.updateSeminarRequestState(id, SeminarStatus.REJECTED, reason, staffId);
    }
}
