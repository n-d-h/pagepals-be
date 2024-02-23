package com.pagepal.capstone.dtos.workingtime;

import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot {
    private UUID id;
    private String startTime;
}
