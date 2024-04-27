package com.pagepal.capstone.dtos.analytic.reader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReaderStatistics {
    private List<String> milestones;
    private List<Integer> completedBookingData;
    private List<Integer> canceledBookingData;
    private Integer successBookingRate;
    private Integer totalFinishBookingInThisPeriod;
    private String totalIncomeInThisPeriod;
    private String totalAmountShareInThisPeriod;
    private String totalRefundInThisPeriod;
    private String totalProfitInThisPeriod;
    private Integer allTimeTotalFinishBooking;
    private String allTimeIncome;
    private Integer totalActiveServices;
}
