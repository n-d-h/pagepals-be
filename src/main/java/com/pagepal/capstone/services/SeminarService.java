package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.seminar.ListSeminarDto;
import com.pagepal.capstone.dtos.seminar.SeminarCreateDto;
import com.pagepal.capstone.dtos.seminar.SeminarDto;
import com.pagepal.capstone.dtos.seminar.SeminarUpdateDto;

import java.util.UUID;

public interface SeminarService {
    SeminarDto createSeminar(SeminarCreateDto seminarCreateDto);
    SeminarDto updateSeminar(UUID readerId, UUID id, SeminarUpdateDto seminarUpdateDto);
    SeminarDto getSeminar(UUID id);
    ListSeminarDto getSeminarList(Integer page, Integer pageSize, String sort);
    ListSeminarDto getSeminarListByReaderId(UUID readerId, Integer page, Integer pageSize, String sort);
    SeminarDto deleteSeminar(UUID id);
}
