package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.report.*;
import com.pagepal.capstone.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @QueryMapping
    public ReportReadDto getReportById(@Argument("id") UUID id) {
        return reportService.getReportById(id);
    }

    @QueryMapping
    public ListReportDto listReport(@Argument("query") ReportQueryDto query){
        return reportService.getAllReports(query);
    }

    @MutationMapping
    public ReportReadDto createReport(@Argument("input") ReportCreateDto report){
        return reportService.createReport(report);
    }

    @QueryMapping
    public List<ReportBookingDto> listReportBooking(){
        return reportService.listReportBooking();
    }

    @QueryMapping
    public List<ReportReaderDto> listReportReader(){
        return reportService.listReportReader();
    }

    @QueryMapping
    public ReportGenericDto getReportGenericByIdAndType(@Argument("id") UUID id, @Argument("type") String reportType){
        return reportService.getReportGenericByReportedIdAndType(id, reportType);
    }

    @MutationMapping
    public ReportReadDto refundBookingForReport(@Argument("id") UUID id){
        return reportService.refundBookingForReport(id);
    }

    @MutationMapping
    public ReportReadDto rejectReport(@Argument("id") UUID id){
        return reportService.rejectReport(id);
    }
}
