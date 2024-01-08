package com.pagepal.capstone.dtos.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryDto {
    private String search;
    private String sort;
    private Integer page;
    private Integer pageSize;
}
