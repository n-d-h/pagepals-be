package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.reader.ReaderDto;
import com.pagepal.capstone.services.ReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ReaderController {

    private final ReaderService readerService;

    @QueryMapping
    public List<ReaderDto> getReadersActive() {
        return readerService.getReadersActive();
    }

    @QueryMapping
    public ReaderDto getReaderDetail(@Argument UUID id) {
        return readerService.getReaderById(id);
    }
}
