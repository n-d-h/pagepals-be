package com.pagepal.capstone.dtos.reader;

import com.pagepal.capstone.dtos.workingtime.WorkingTimeListRead;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReaderProfileDto {
    private ReaderDto profile;
    private WorkingTimeListRead workingTimeList;

}
