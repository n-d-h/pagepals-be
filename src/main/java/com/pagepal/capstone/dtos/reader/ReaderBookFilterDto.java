package com.pagepal.capstone.dtos.reader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReaderBookFilterDto {
    private String title;
    private Integer page;
    private Integer pageSize;
}
