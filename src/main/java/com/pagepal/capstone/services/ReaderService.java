package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.reader.ReaderDto;

import java.util.List;
import java.util.UUID;

public interface ReaderService {

    List<ReaderDto> getReadersActive();

    ReaderDto getReaderById(UUID id);
}
