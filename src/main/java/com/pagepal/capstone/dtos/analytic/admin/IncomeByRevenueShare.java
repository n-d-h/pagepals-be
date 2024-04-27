package com.pagepal.capstone.dtos.analytic.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IncomeByRevenueShare {
    private float totalIncome;
    private float percentageIncrease;
    private List<IncomeByRevenueShareData> seriesData;
}
