package com.pagepal.capstone.dtos.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateDto {
    private UUID seminarId;
    private String startAt;
    private Integer limitCustomer;
    private Integer price;
    private Boolean isFeatured;
    private String advertiseStartAt;
    private String advertiseEndAt;
}
