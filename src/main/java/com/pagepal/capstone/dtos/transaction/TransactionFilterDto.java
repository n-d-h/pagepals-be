package com.pagepal.capstone.dtos.transaction;

import com.pagepal.capstone.enums.TransactionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionFilterDto {
    private String startDate;
    private String endDate;
    private String transactionType;
    private Integer page;
    private Integer pageSize;
}
