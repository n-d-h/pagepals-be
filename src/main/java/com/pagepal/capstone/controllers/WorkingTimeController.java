package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.workingtime.WorkingTimeListCreateDto;
import com.pagepal.capstone.services.WorkingTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WorkingTimeController {
    private final WorkingTimeService workingTimeService;

    @MutationMapping
    public String createReaderWorkingTime(
            @Argument(name = "workingTime") WorkingTimeListCreateDto list) {
        return workingTimeService.createReaderWorkingTime(list);
    }
}
