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
public class WithdrawRequestReadDto {

    private UUID id;

    private Float amount;

    private Date createdAt;

    private Date updatedAt;

    private String bankName;

    private String bankAccountNumber;

    private String bankAccountName;

    private String transactionImage;

    private String rejectReason;

    private UUID staffId;

    private String staffName;

    private WithdrawRequestStateEnum state;

    private Reader reader;
}
