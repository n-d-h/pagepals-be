package com.pagepal.capstone.dtos.event;

import java.util.List;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListEventDto {
	private PagingDto pagination;
	private List<EventDto> list;
}
