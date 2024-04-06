package com.pagepal.capstone.dtos.transaction;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListTransactionDto {
    private List<TransactionDto> list;
    private PagingDto paging;
}
