package com.pagepal.capstone.dtos.workingtime;

import com.pagepal.capstone.dtos.reader.ReaderDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingTimeDto {
    private UUID id;
    private Date date;
    private Date startTime;
    private Date endTime;
    private ReaderDto reader;
}
