package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.account.AccountRequest;
import com.pagepal.capstone.dtos.account.AccountResponse;
import com.pagepal.capstone.dtos.account.RefreshTokenRequest;
import com.pagepal.capstone.dtos.account.RegisterRequest;
import com.pagepal.capstone.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @MutationMapping(name = "login")
    public AccountResponse login(@Argument(name = "account") AccountRequest accountRequest) {
        return accountService.authenticate(accountRequest);
    }

    @MutationMapping(name = "register")
    public AccountResponse register(@Argument(name = "register") RegisterRequest registerRequest) {
        return accountService.register(registerRequest);
    }

    @MutationMapping(name = "refreshToken")
    public AccountResponse refresh(@ContextValue String authorization) {
        String refreshToken = authorization.substring(7);
        return accountService.refresh(new RefreshTokenRequest(refreshToken));
    }
}
