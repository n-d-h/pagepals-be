package com.pagepal.capstone.dtos.reader;

import com.pagepal.capstone.dtos.book.BookDto;
import com.pagepal.capstone.dtos.service.ServiceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReaderBookDto {
    private BookDto book;
    private List<ServiceDto> services = new ArrayList<>();
}
