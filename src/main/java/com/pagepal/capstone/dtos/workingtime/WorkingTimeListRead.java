package com.pagepal.capstone.dtos.workingtime;

import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkingTimeListRead {

    private List<WorkingDate> workingDates;
}
