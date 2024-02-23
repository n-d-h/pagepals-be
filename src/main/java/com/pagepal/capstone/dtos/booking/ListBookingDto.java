package com.pagepal.capstone.dtos.booking;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import lombok.AllArgsConstructor;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ListBookingDto {
    private PagingDto pagination;
    private List<BookingDto> list;
}
