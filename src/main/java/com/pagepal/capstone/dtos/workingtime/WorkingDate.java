package com.pagepal.capstone.dtos.workingtime;

import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkingDate {
    private Date date;
    private List<TimeSlot> timeSlots;
}
