package com.pagepal.capstone.dtos.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportQueryDto {
    private Integer page;
    private Integer pageSize;
    private String type;
    private String state;
}
