package com.pagepal.capstone.dtos.withdrawRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawRequestCreateDto {

    private Float amount;

    private String bankName;

    private String bankAccountNumber;

    private String bankAccountName;

}
