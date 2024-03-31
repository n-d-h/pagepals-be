package com.pagepal.capstone.dtos.analytic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IncomeByToken {
    private int totalTokenSale;
    private float totalIncome;
    private float percentageIncrease;
    private List<IncomeByTokenData> seriesData;
}
