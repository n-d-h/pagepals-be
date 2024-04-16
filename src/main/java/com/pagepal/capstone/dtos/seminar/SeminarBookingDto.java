package com.pagepal.capstone.dtos.seminar;

import com.pagepal.capstone.entities.postgre.Booking;
import com.pagepal.capstone.entities.postgre.Seminar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeminarBookingDto {
    private Seminar seminar;
    private Booking booking;
}
