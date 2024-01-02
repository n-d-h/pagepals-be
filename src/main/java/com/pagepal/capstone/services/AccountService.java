package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.account.AccountRequest;
import com.pagepal.capstone.dtos.account.AccountResponse;
import com.pagepal.capstone.dtos.account.RefreshTokenRequest;
import com.pagepal.capstone.dtos.account.RegisterRequest;

public interface AccountService {
    AccountResponse register(RegisterRequest request);
    AccountResponse authenticate(AccountRequest request);
    AccountResponse refresh(RefreshTokenRequest request);
}
