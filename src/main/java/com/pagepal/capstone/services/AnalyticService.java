package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.analytic.admin.AnalyticAdmin;
import com.pagepal.capstone.dtos.analytic.reader.ReaderStatistics;

import java.util.UUID;

public interface AnalyticService {
    AnalyticAdmin getAnalyticAdmin();

    AnalyticAdmin getAnalyticAdminByDate(String startDate, String endDate);

    ReaderStatistics getReaderStatistics(UUID id, String startDate, String endDate, Boolean isMobile);
}
