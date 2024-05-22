package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.seminar.ListSeminarDto;
import com.pagepal.capstone.dtos.seminar.SeminarCreateDto;
import com.pagepal.capstone.dtos.seminar.SeminarDto;
import com.pagepal.capstone.dtos.seminar.SeminarUpdateDto;
import com.pagepal.capstone.enums.SeminarStatus;

import java.util.UUID;

public interface SeminarService {
    SeminarDto createSeminarRequest(SeminarCreateDto seminarCreateDto);

    SeminarDto updateSeminarRequest(SeminarUpdateDto seminarUpdateDto);

    SeminarDto deleteSeminarRequest(UUID id);

    ListSeminarDto getAllSeminarRequests(Integer page, Integer pageSize, String sort);

    ListSeminarDto getAllSeminarRequestsByState(Integer page, Integer pageSize, String sort, SeminarStatus state);

    SeminarDto getSeminarRequest(UUID id);

    ListSeminarDto getAllSeminarRequestsByReaderId(UUID readerId, Integer page, Integer pageSize, String sort);

    ListSeminarDto getAllSeminarRequestsByReaderIdAndState(UUID readerId, Integer page, Integer pageSize, String sort, SeminarStatus state, String search);

    SeminarDto updateSeminarRequestState(UUID id, SeminarStatus state, String description, UUID staffId);
}
