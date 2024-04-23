package com.pagepal.capstone.dtos.workingtime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkingTimeCreateDto {
    private String date;
    private String startTime;
    private Integer duration;
}
