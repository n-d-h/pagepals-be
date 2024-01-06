package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.reader.ReaderDto;
import com.pagepal.capstone.dtos.reader.ReaderProfileDto;
import com.pagepal.capstone.dtos.reader.ReaderQueryDto;
import com.pagepal.capstone.dtos.service.ServiceDto;

import java.util.List;
import java.util.UUID;

public interface ReaderService {

    List<ReaderDto> getReadersActive();

    ReaderDto getReaderById(UUID id);

    List<ReaderDto> getListReaders(ReaderQueryDto readerQueryDto);

    List<ServiceDto> getListServicesByReaderId(UUID id);

    ReaderProfileDto getReaderProfileById(UUID id);
}
