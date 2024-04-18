package com.pagepal.capstone.dtos.reader;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListReaderUpdateRequestDto {
    List<ReaderRequestInputDto> list;
    PagingDto pagination;
}
