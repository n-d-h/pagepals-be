package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.account.*;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    String verifyEmailRegister(RegisterRequest request) throws Exception;
    AccountResponse register(RegisterRequest request);
    AccountResponse authenticate(AccountRequest request);
    AccountResponse authenticateWithGoogle(String token);
    AccountResponse refresh(String token);
    AccountDto getAccountById(UUID id);
    AccountDto registerStaff(AccountStaffCreateDto account);
    List<AccountDto> getListStaff();
    List<AccountDto> getListCustomer();
    List<AccountDto> getListReader();
    AccountDto updateAccount(UUID id, AccountUpdateDto accountUpdateDto);
    AccountReadDto getAccountByUsername(String username);
    AccountDto updateAccountState(UUID id, String accountState);
}
