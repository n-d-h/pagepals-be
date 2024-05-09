package com.pagepal.capstone.dtos.workingtime;

import com.pagepal.capstone.dtos.reader.ReaderDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkingTimeDto {
    private UUID id;
    private Date date;
    private Date startTime;
    private Date endTime;
    private ReaderDto reader;
    private Boolean isBooked;
}
