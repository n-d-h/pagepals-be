package com.pagepal.capstone.dtos.withdrawRequest;

import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.enums.WithdrawRequestStateEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

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
