package com.pagepal.capstone.dtos.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QueryDto {
    private String sort;
    private Integer page;
    private Integer pageSize;
}
