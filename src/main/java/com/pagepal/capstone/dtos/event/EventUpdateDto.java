package com.pagepal.capstone.dtos.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdateDto {
    private String startAt;
    private Integer limitCustomer;
    private Integer price;
}
