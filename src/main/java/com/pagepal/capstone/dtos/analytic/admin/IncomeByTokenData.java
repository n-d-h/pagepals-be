package com.pagepal.capstone.dtos.analytic.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IncomeByTokenData {
    private int token;
    private float income;
    private String date;
}
