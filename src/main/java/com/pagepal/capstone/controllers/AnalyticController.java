package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.analytic.admin.AnalyticAdmin;
import com.pagepal.capstone.dtos.analytic.reader.ReaderStatistics;
import com.pagepal.capstone.services.AnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AnalyticController {
    private final AnalyticService analyticService;

    @QueryMapping
    public AnalyticAdmin getAnalyticAdmin(@Argument("startDate") String startDate,
                                          @Argument("endDate") String endDate) {
        return analyticService.getAnalyticAdminByDate(startDate, endDate);
    }

    @QueryMapping
    public ReaderStatistics getReaderStatistics(@Argument("readerId") UUID id,
                                                @Argument("startDate") String startDate,
                                                @Argument("endDate") String endDate,
                                                @Argument("isMobile") Boolean isMobile) {
        return analyticService.getReaderStatistics(id, startDate, endDate, isMobile);
    }
}
