package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.accountstate.AccountStateRead;

import java.util.List;

public interface AccountStateService {
    List<AccountStateRead> getAccountStates();
}
