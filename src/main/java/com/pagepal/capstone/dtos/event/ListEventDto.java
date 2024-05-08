package com.pagepal.capstone.dtos.event;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListEventDto {
	private PagingDto pagination;
	private List<EventDto> list;
}
