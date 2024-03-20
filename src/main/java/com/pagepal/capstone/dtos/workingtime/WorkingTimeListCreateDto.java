package com.pagepal.capstone.dtos.workingtime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkingTimeListCreateDto {
    private UUID readerId;
    private List<WorkingTimeCreateDto> list;
    private Boolean isWeekly;
}
