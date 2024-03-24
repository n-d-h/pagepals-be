package com.pagepal.capstone.dtos.service;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ListService {
    private List<ServiceDto> services;
    private PagingDto paging;
}
