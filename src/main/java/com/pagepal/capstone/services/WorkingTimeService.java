package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.workingtime.WorkingTimeListCreateDto;

public interface WorkingTimeService {

    String createReaderWorkingTime(WorkingTimeListCreateDto list);
}
