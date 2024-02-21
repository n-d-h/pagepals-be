package com.pagepal.capstone.dtos.workingtime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkingTimeListRead {

    private List<WorkingDate> workingDates;
}
