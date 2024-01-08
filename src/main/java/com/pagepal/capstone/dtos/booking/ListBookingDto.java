package com.pagepal.capstone.dtos.booking;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListBookingDto {
    private PagingDto pagination;
    private List<BookingDto> list;
}
