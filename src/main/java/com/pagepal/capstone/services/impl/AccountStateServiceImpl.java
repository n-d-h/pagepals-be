package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.accountstate.AccountStateRead;
import com.pagepal.capstone.mappers.AccountStateMapper;
import com.pagepal.capstone.repositories.postgre.AccountStateRepository;
import com.pagepal.capstone.services.AccountStateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountStateServiceImpl implements AccountStateService {

    private final AccountStateRepository accountStateRepository;

    @Override
    @Secured({"ADMIN", "STAFF"})
    public List<AccountStateRead> getAccountStates() {
        return accountStateRepository.findAll().stream().map(AccountStateMapper.INSTANCE::toDto).toList();
    }
}
