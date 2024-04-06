package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.analytic.AnalyticAdmin;

public interface AnalyticService {
    AnalyticAdmin getAnalyticAdmin();

    AnalyticAdmin getAnalyticAdminByDate(String startDate, String endDate);
}
