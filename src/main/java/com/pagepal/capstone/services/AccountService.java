package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.account.*;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountResponse register(RegisterRequest request);
    AccountResponse authenticate(AccountRequest request);
    AccountResponse refresh(RefreshTokenRequest request);
    AccountDto getAccountById(UUID id);
    AccountStaffResponse registerStaff(String username);
    List<AccountDto> getListStaff();
}
