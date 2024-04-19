package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.report.ListReportDto;
import com.pagepal.capstone.dtos.report.ReportCreateDto;
import com.pagepal.capstone.dtos.report.ReportQueryDto;
import com.pagepal.capstone.dtos.report.ReportReadDto;
import com.pagepal.capstone.dtos.seminar.ReportBookingDto;
import com.pagepal.capstone.dtos.seminar.ReportPostDto;
import com.pagepal.capstone.dtos.seminar.ReportReaderDto;

import java.util.List;
import java.util.UUID;

public interface ReportService {

    ReportReadDto getReportById(UUID id);

    ReportReadDto createReport(ReportCreateDto reportCreateDto);

    ListReportDto getAllReports(ReportQueryDto reportQueryDto);
    List<ReportBookingDto> listReportBooking();
    List<ReportReaderDto> listReportReader();
    List<ReportPostDto> listReportPost();

}
