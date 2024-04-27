package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.setting.SettingDto;
import com.pagepal.capstone.dtos.transaction.ListTransactionDto;
import com.pagepal.capstone.dtos.transaction.TransactionDto;
import com.pagepal.capstone.dtos.transaction.TransactionFilterDto;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
     ListTransactionDto getListTransactionForCustomer(UUID customerId, TransactionFilterDto filter) throws ParseException;
     ListTransactionDto getListTransactionForReader(UUID readerId, TransactionFilterDto filter) throws ParseException;

     TransactionDto getTransactionById(UUID id);

     List<SettingDto> getAllSettings();
}
