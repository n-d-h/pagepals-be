package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.reader.ReaderDto;

import java.util.List;

public interface ReaderService {

    List<ReaderDto> getReadersActive();
}
