package com.pagepal.capstone.dtos.seminar;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListSeminarDto {
    private List<SeminarDto> list;
    private PagingDto pagination;
}
