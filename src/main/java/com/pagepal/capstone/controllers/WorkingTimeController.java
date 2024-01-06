package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.workingtime.WorkingTimeCreateDto;
import com.pagepal.capstone.dtos.workingtime.WorkingTimeDto;
import com.pagepal.capstone.services.WorkingTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WorkingTimeController {
    private final WorkingTimeService workingTimeService;

    @MutationMapping(name = "createReaderWorkingTime")
    public WorkingTimeDto createReaderWorkingTime(
            @Argument(name = "workingTime") WorkingTimeCreateDto workingTimeCreateDto) {
        return workingTimeService.createReaderWorkingTime(workingTimeCreateDto);
    }
}
