package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.reader.*;
import com.pagepal.capstone.dtos.request.RequestInputDto;
import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.dtos.workingtime.WorkingTimeListRead;

import java.util.List;
import java.util.UUID;

public interface ReaderService {

    List<ReaderDto> getReadersActive();

    ReaderDto getReaderById(UUID id);

    ListReaderDto getListReaders(ReaderQueryDto readerQueryDto);

    List<ReaderDto> getListPopularReaders();

    List<ServiceDto> getListServicesByReaderId(UUID id);

    ReaderProfileDto getReaderProfileById(UUID id);

    ReaderProfileDto updateReaderProfile(UUID id, ReaderUpdateDto readerUpdateDto);

    WorkingTimeListRead getWorkingTimesAvailableByReader(UUID id);

    List<ReaderBookDto> getBookOfReader(UUID id);

    ReaderDto registerReader(UUID accountId,RequestInputDto requestInputDto);
}
