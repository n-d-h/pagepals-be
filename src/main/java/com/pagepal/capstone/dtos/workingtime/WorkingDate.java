package com.pagepal.capstone.dtos.workingtime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkingDate {
    private String date;
    private List<TimeSlot> timeSlots;
}
