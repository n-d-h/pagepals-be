package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.event.*;

import java.util.UUID;

public interface EventService {
    EventDto createEvent(EventCreateDto eventDto, UUID readerId);
    EventDto updateEvent(UUID id, EventUpdateDto eventDto);
    EventDto getEvent(UUID id);
    EventDto deleteEvent(UUID id);
    EventBookingDto bookEvent(UUID eventId, UUID customerId);
    EventDto advertiseEvent(UUID eventId, UUID readerId, String advertiseAt);
    ListEventDto getAllEventBySeminarId(UUID seminarId, Integer page, Integer pageSize, String sort);
    ListEventDto getAllEvent(Integer page, Integer pageSize, String sort);
    ListEventDto getAllEventNotJoinByCustomer(UUID customerId, Integer page, Integer pageSize, String sort);
    ListEventDto getAllEventByReader(UUID readerId, Integer page, Integer pageSize, String sort);
    ListEventDto getAllActiveEventByReader(UUID readerId, Integer page, Integer pageSize, String sort);
}
