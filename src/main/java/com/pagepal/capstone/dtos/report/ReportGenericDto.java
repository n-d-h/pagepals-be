package com.pagepal.capstone.dtos.report;

import com.pagepal.capstone.dtos.booking.BookingDto;
import com.pagepal.capstone.dtos.reader.ReaderDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportGenericDto {
    private BookingDto booking;
    private ReaderDto reader;
    private List<ReportReadDto> listReport;
}
