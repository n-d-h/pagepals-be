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

    String updateReaderProfile(UUID id, ReaderRequestInputDto readerUpdateDto);

    WorkingTimeListRead getWorkingTimesAvailableByReader(UUID id);

    ReaderBookListDto getBookOfReader(UUID id, ReaderBookFilterDto readerBookFilterDto);

    ReaderDto registerReader(UUID accountId,RequestInputDto requestInputDto);

    ListReaderReviewDto getReaderReviewsByReaderId(UUID id, Integer page, Integer size);

    ReaderRequestReadDto getUpdateRequestByReaderId(UUID readerId);

    ListReaderUpdateRequestDto getAllUpdateRequestedReader(Integer page, Integer pageSize);
    ReaderDto acceptUpdateReader(UUID id);
    ReaderDto rejectUpdateReader(UUID id);
}
