package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.workingtime.WorkingTimeListCreateDto;

import java.util.UUID;

public interface WorkingTimeService {

    String createReaderWorkingTime(WorkingTimeListCreateDto list);

    Boolean deleteReaderWorkingTime(UUID id);
}
