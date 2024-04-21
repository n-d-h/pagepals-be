package com.pagepal.capstone.dtos.withdrawRequest;

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
public class ListWithdrawRequestDto {
    private List<WithdrawRequestReadDto> list;
    private PagingDto paging;
}
