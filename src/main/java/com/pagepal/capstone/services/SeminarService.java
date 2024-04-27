package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.seminar.*;

import java.util.UUID;

public interface SeminarService {
    SeminarDto createSeminar(SeminarCreateDto seminarCreateDto);
    SeminarDto updateSeminar(UUID readerId, UUID id, SeminarUpdateDto seminarUpdateDto);
    SeminarDto getSeminar(UUID id);
    ListSeminarDto getSeminarList(Integer page, Integer pageSize, String sort, String state);
    ListSeminarDto getSeminarListByReaderId(UUID readerId, Integer page, Integer pageSize, String sort, String state);
    ListSeminarDto getSeminarListByCustomerId(UUID customerId, Integer page, Integer pageSize, String sort);
    ListSeminarDto getSeminarListNotJoinByCustomerId(UUID customerId, Integer page, Integer pageSize, String sort, String state);
    SeminarDto deleteSeminar(UUID id);
    SeminarBookingDto bookSeminar(UUID seminarId, UUID customerId);
}
