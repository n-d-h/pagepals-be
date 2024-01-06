package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.workingtime.WorkingTimeCreateDto;
import com.pagepal.capstone.dtos.workingtime.WorkingTimeDto;

public interface WorkingTimeService {

    WorkingTimeDto createReaderWorkingTime(WorkingTimeCreateDto workingTimeCreateDto);
}
