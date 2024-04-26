package com.pagepal.capstone.dtos.analytic;

import com.pagepal.capstone.dtos.reader.ReaderDto;
import com.pagepal.capstone.dtos.service.ServiceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticAdmin {
    private long totalCustomers;
    private long totalReaders;
    private long totalService;
    private long totalBookings;
    private BookingStatics bookingStatics;
    private IncomeByToken incomeByToken;
    private CirculatingToken circulatingToken;
    private IncomeByRevenueShare incomeByRevenueShare;
    private FreeStorage freeStorage;
    private CloudStorage cloudStorage;
    private List<TopReader> topReaders;
    private List<TopService> topServices;
}
