package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.workingtime.WorkingTimeCreateDto;
import com.pagepal.capstone.dtos.workingtime.WorkingTimeDto;
import com.pagepal.capstone.dtos.workingtime.WorkingTimeListCreateDto;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.WorkingTime;
import com.pagepal.capstone.mappers.WorkingTimeMapper;
import com.pagepal.capstone.repositories.postgre.ReaderRepository;
import com.pagepal.capstone.repositories.postgre.WorkingTimeRepository;
import com.pagepal.capstone.services.WorkingTimeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkingTimeServiceImpl implements WorkingTimeService {

    private final WorkingTimeRepository workingTimeRepository;
    private final ReaderRepository readerRepository;

    @Secured("READER")
    @Override
    public String createReaderWorkingTime(WorkingTimeListCreateDto list) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Reader reader = readerRepository.findById(list.getReaderId())
                .orElseThrow(() -> new EntityNotFoundException("Reader not found"));

        for (WorkingTimeCreateDto wt : list.getList()) {
            LocalDate startDate = LocalDate.parse(wt.getDate());
            LocalDateTime startTime = LocalDateTime.parse(wt.getStartTime(), formatter);
            LocalDateTime endTime = startTime.plusMinutes(wt.getDuration());

            if (list.getIsWeekly()) {
                LocalDate endDate = startDate.plusWeeks(1); // Calculate the end date of the first week

                while (startDate.isBefore(LocalDate.now().plusMonths(3))) {
                    // Create working time slot for the current week
                    createWorkingTimeSlot(startDate, endDate, startTime, endTime, reader);

                    // Move to the next week
                    startDate = startDate.plusWeeks(1);
                    endDate = endDate.plusWeeks(1);
                }
            } else {
                // Create working time slot for a single occurrence
                createWorkingTimeSlot(startDate, startDate.plusDays(1), startTime, endTime, reader);
            }
        }

        return "Working time created successfully";
    }

    private void createWorkingTimeSlot(LocalDate date, LocalDate endDate, LocalDateTime startTime, LocalDateTime endTime, Reader reader) {
        WorkingTime workingTime = new WorkingTime();
        workingTime.setStartTime(Date.from(startTime.atZone(java.time.ZoneId.systemDefault()).toInstant()));
        workingTime.setEndTime(Date.from(endTime.atZone(java.time.ZoneId.systemDefault()).toInstant()));
        workingTime.setDate(Date.from(date.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        workingTime.setReader(reader);
        workingTimeRepository.save(workingTime);
    }
}
