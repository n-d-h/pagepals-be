package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.reader.*;
import com.pagepal.capstone.dtos.request.RequestInputDto;
import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.dtos.workingtime.WorkingTimeListRead;
import com.pagepal.capstone.services.ReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
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

    @QueryMapping
    public ListReaderDto getListReaders(@Argument(name = "query") ReaderQueryDto readerQueryDto) {
        return readerService.getListReaders(readerQueryDto);
    }

    @QueryMapping
    public List<ReaderDto> getListPopularReaders() {
        return readerService.getListPopularReaders();
    }

    @QueryMapping
    public List<ServiceDto> getListServicesOfReader(@Argument UUID id) {
        return readerService.getListServicesByReaderId(id);
    }

    @QueryMapping
    public ReaderProfileDto getReaderProfile(@Argument UUID id) {
        return readerService.getReaderProfileById(id);
    }

    @MutationMapping
    public String updateReader(@Argument UUID id, @Argument(name = "data") ReaderRequestInputDto readerProfileDto) {
        return readerService.updateReaderProfile(id, readerProfileDto);
    }

    @QueryMapping
    public WorkingTimeListRead getWorkingTimesAvailableByReader(@Argument(name = "readerId") UUID id) {
        return readerService.getWorkingTimesAvailableByReader(id);
    }

    @QueryMapping
    public ReaderBookListDto getReaderBooks(@Argument("id") UUID id,
                                            @Argument(name = "filter") ReaderBookFilterDto filter) {
        return readerService.getBookOfReader(id, filter);
    }

    @MutationMapping
    public ReaderDto registerReader(@Argument(name = "accountId") UUID id, @Argument(name = "data") RequestInputDto readerRequestInputDto) {
        return readerService.registerReader(id, readerRequestInputDto);
    }

    @QueryMapping
    public ListReaderReviewDto getReaderReviews(@Argument(name = "readerId") UUID id,
                                                @Argument(name = "page") Integer page,
                                                @Argument(name = "pageSize") Integer pageSize) {
        return readerService.getReaderReviewsByReaderId(id, page, pageSize);
    }
}
