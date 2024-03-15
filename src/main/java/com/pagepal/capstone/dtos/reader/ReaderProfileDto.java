package com.pagepal.capstone.dtos.reader;

import com.pagepal.capstone.dtos.workingtime.WorkingTimeListRead;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReaderProfileDto {
    private ReaderDto profile;
    private WorkingTimeListRead workingTimeList;

}
