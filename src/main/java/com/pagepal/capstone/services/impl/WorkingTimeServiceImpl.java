package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.reader.ReaderProfileDto;
import com.pagepal.capstone.dtos.workingtime.WorkingTimeCreateDto;
import com.pagepal.capstone.dtos.workingtime.WorkingTimeDto;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.WorkingTime;
import com.pagepal.capstone.mappers.WorkingTimeMapper;
import com.pagepal.capstone.repositories.postgre.ReaderRepository;
import com.pagepal.capstone.repositories.postgre.WorkingTimeRepository;
import com.pagepal.capstone.services.WorkingTimeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
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
    public WorkingTimeDto createReaderWorkingTime(WorkingTimeCreateDto workingTimeCreateDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDate date = LocalDate.parse(workingTimeCreateDto.getDate());
        LocalDateTime startTime = LocalDateTime.parse(workingTimeCreateDto.getStartTime(), formatter);
        LocalDateTime endTime = LocalDateTime.parse(workingTimeCreateDto.getEndTime(), formatter);
        Optional<Reader> reader = readerRepository.findById(workingTimeCreateDto.getReaderId());

        if(reader.isEmpty()){
            throw new EntityNotFoundException("Reader not found");
        }

        WorkingTime workingTime = new WorkingTime();
        workingTime.setStartTime(Date.from(startTime.atZone(java.time.ZoneId.systemDefault()).toInstant()));
        workingTime.setEndTime(Date.from(endTime.atZone(java.time.ZoneId.systemDefault()).toInstant()));
        workingTime.setDate(Date.from(date.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        workingTime.setReader(reader.get());

        WorkingTime saveWorkingTime = workingTimeRepository.save(workingTime);
        return WorkingTimeMapper.INSTANCE.toDto(saveWorkingTime);
    }
}
