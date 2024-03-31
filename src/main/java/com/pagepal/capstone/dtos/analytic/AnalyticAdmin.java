package com.pagepal.capstone.dtos.analytic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private IncomeByRevenueShare incomeByRevenueShare;
}
