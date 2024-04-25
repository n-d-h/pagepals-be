package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.report.*;

import java.util.List;
import java.util.UUID;

public interface ReportService {

    ReportReadDto getReportById(UUID id);

    ReportReadDto createReport(ReportCreateDto reportCreateDto);

    ListReportDto getAllReports(ReportQueryDto reportQueryDto);
    List<ReportBookingDto> listReportBooking();
    List<ReportReaderDto> listReportReader();
    ReportGenericDto getReportGenericByReportedIdAndType(UUID id, String reportType);

    ReportReadDto refundBookingForReport(UUID id);

    ReportReadDto rejectReport(UUID id);


}
