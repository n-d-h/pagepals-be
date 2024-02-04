package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.accountstate.AccountStateRead;
import com.pagepal.capstone.services.AccountStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountStateController {

    private final AccountStateService accountStateService;

    @QueryMapping
    public List<AccountStateRead> getListAccountState(){
        return accountStateService.getAccountStates();
    }
}
