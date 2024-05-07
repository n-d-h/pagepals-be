package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.seminar.*;
import com.pagepal.capstone.enums.SeminarStatus;

import java.util.UUID;

public interface SeminarService {
    SeminarDto createSeminarRequest(SeminarCreateDto seminarCreateDto);
    SeminarDto updateSeminarRequest(SeminarUpdateDto seminarUpdateDto);
    SeminarDto deleteSeminarRequest(UUID id);
    ListSeminarDto getAllSeminarRequests(Integer page, Integer pageSize, String sort, SeminarStatus state);
    SeminarDto getSeminarRequest(UUID id);
//    SeminarDto getSeminar(UUID id);
//    ListSeminarDto getSeminarList(Integer page, Integer pageSize, String sort, String state);
//    ListSeminarDto getSeminarListByReaderId(UUID readerId, Integer page, Integer pageSize, String sort, String state, Boolean isCustomer);
//    ListSeminarDto getSeminarListByCustomerId(UUID customerId, Integer page, Integer pageSize, String sort);
//    ListSeminarDto getSeminarListNotJoinByCustomerId(UUID customerId, Integer page, Integer pageSize, String sort, String state);
//    SeminarDto deleteSeminar(UUID id);
//    SeminarBookingDto bookSeminar(UUID seminarId, UUID customerId);
//    SeminarDto completeSeminar(UUID seminarId);
}
