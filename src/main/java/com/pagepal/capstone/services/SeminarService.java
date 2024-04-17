package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.booking.BookingDto;
import com.pagepal.capstone.dtos.seminar.*;

import java.util.UUID;

public interface SeminarService {
    SeminarDto createSeminar(SeminarCreateDto seminarCreateDto);
    SeminarDto updateSeminar(UUID readerId, UUID id, SeminarUpdateDto seminarUpdateDto);
    SeminarDto getSeminar(UUID id);
    ListSeminarDto getSeminarList(Integer page, Integer pageSize, String sort);
    ListSeminarDto getSeminarListByReaderId(UUID readerId, Integer page, Integer pageSize, String sort);
    SeminarDto deleteSeminar(UUID id);
    SeminarBookingDto bookSeminar(UUID seminarId, UUID customerId);
}
