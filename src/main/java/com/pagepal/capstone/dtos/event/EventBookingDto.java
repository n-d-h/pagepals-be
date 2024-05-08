package com.pagepal.capstone.dtos.event;

import com.pagepal.capstone.entities.postgre.Booking;
import com.pagepal.capstone.entities.postgre.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventBookingDto {
    private Event event;
    private Booking booking;
}
