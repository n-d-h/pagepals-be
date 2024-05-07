package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.seminar.*;
import com.pagepal.capstone.enums.SeminarStatus;

import java.util.List;
import java.util.UUID;

public interface SeminarService {
    SeminarDto createSeminarRequest(SeminarCreateDto seminarCreateDto);

    SeminarDto updateSeminarRequest(SeminarUpdateDto seminarUpdateDto);

    SeminarDto deleteSeminarRequest(UUID id);

    ListSeminarDto getAllSeminarRequests(Integer page, Integer pageSize, String sort);

    ListSeminarDto getAllSeminarRequestsByState(Integer page, Integer pageSize, String sort, SeminarStatus state);

    SeminarDto getSeminarRequest(UUID id);

    ListSeminarDto getAllSeminarRequestsByReaderId(UUID readerId, Integer page, Integer pageSize, String sort);

    ListSeminarDto getAllSeminarRequestsByReaderIdAndState(UUID readerId, Integer page, Integer pageSize, String sort, SeminarStatus state);
}
