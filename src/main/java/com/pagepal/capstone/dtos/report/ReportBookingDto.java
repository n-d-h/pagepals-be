package com.pagepal.capstone.dtos.report;

import com.pagepal.capstone.dtos.booking.BookingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportBookingDto {
    private BookingDto booking;
    private List<ReportReadDto> listReport;
}
