package com.pagepal.capstone.dtos.transaction;

import com.pagepal.capstone.entities.postgre.Booking;
import com.pagepal.capstone.entities.postgre.PaymentMethod;
import com.pagepal.capstone.entities.postgre.Wallet;
import com.pagepal.capstone.enums.CurrencyEnum;
import com.pagepal.capstone.enums.TransactionStatusEnum;
import com.pagepal.capstone.enums.TransactionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private UUID id;

    private String description;

    private String createAt;

    private Double amount;

    private CurrencyEnum currency;

    private TransactionTypeEnum transactionType;

    private TransactionStatusEnum status;

    private Booking booking;

    private PaymentMethod paymentMethod;

    private Wallet wallet;
}
