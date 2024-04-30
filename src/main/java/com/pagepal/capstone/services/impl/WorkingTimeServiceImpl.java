package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.workingtime.WorkingTimeCreateDto;
import com.pagepal.capstone.dtos.workingtime.WorkingTimeListCreateDto;
import com.pagepal.capstone.entities.postgre.Booking;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.WorkingTime;
import com.pagepal.capstone.repositories.ReaderRepository;
import com.pagepal.capstone.repositories.WorkingTimeRepository;
import com.pagepal.capstone.services.WorkingTimeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkingTimeServiceImpl implements WorkingTimeService {

    private final WorkingTimeRepository workingTimeRepository;
    private final ReaderRepository readerRepository;

    @Secured("READER")
    @Override
    public String createReaderWorkingTime(WorkingTimeListCreateDto list) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId vietnamTimeZone = ZoneId.of("Asia/Ho_Chi_Minh");
        Reader reader = readerRepository.findById(list.getReaderId())
                .orElseThrow(() -> new EntityNotFoundException("Reader not found"));

        for (WorkingTimeCreateDto wt : list.getList()) {
            LocalDate startDate = LocalDate.parse(wt.getDate());
            LocalDateTime startTime = LocalDateTime.parse(wt.getStartTime(), formatter);
            LocalDateTime endTime = startTime.plusMinutes(wt.getDuration());

            if (list.getIsWeekly()) {
                LocalDate endDate = startDate.plusWeeks(1); // Calculate the end date of the first week

                while (startDate.isBefore(LocalDate.now(vietnamTimeZone).plusMonths(3))) {
                    // Create working time slot for the current week
                    createWorkingTimeSlot(startDate, endDate, startTime, endTime, reader);

                    // Move to the next week
                    startTime = startTime.plusWeeks(1);
                    endTime = endTime.plusWeeks(1);
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

    public Boolean deleteReaderWorkingTime(UUID id){
        WorkingTime wt = workingTimeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Working time not found"));
        List<Booking> workingTimeBookings = wt.getBookings();
        if(workingTimeBookings != null && workingTimeBookings.size() > 0){
            throw new ValidationException("Working time have booking, cannot delete");
        }
        workingTimeRepository.delete(wt);
        return true;
    }

    private void createWorkingTimeSlot(LocalDate date, LocalDate endDate, LocalDateTime startTime, LocalDateTime endTime, Reader reader) {
        WorkingTime workingTime = new WorkingTime();
        workingTime.setStartTime(Date.from(startTime.atZone(java.time.ZoneId.systemDefault()).toInstant()));
        workingTime.setEndTime(Date.from(endTime.atZone(java.time.ZoneId.systemDefault()).toInstant()));
        workingTime.setDate(Date.from(date.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        workingTime.setReader(reader);
        Boolean isExist = workingTimeRepository.existsByStartTimeAndEndTimeAndReaderId(workingTime.getStartTime(), workingTime.getEndTime(), reader.getId());
        if(!isExist){
            workingTimeRepository.save(workingTime);
        }
    }
}
