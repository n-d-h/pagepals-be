package com.pagepal.capstone.dtos.booking;

import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QueryDto {
    private String sort;
    private Integer page;
    private Integer pageSize;
}
