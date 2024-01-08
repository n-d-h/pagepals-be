package com.pagepal.capstone.dtos.bookingdetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailCreateDto {
    private Double price;
    private String description;
    private UUID serviceId;
}
