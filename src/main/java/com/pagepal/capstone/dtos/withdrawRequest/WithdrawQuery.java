package com.pagepal.capstone.dtos.withdrawRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawQuery {
    private Integer page;
    private Integer pageSize;
    private String sort;
}
