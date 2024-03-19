package com.pagepal.capstone.dtos.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateDto {
    private UUID workingTimeId;
    private Double totalPrice;
    private String promotionCode;
    private String description;
    private UUID serviceId;
}
