package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.account.*;
import com.pagepal.capstone.services.AccountService;
import graphql.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final HttpServletRequest request;
    private final AccountService accountService;
    private final static String ROLE_CUSTOMER = "CUSTOMER";
    private final static String ROLE_STAFF = "STAFF";

    @MutationMapping(name = "login")
    public AccountResponse login(@Argument(name = "account") AccountRequest accountRequest) {
        return accountService.authenticate(accountRequest);
    }

    @MutationMapping(name = "register")
    public AccountResponse register(@Argument(name = "register") RegisterRequest registerRequest) {
        return accountService.register(registerRequest);
    }

    @MutationMapping(name = "registerStaff")
    public AccountStaffResponse registerStaff(@Argument(name = "username") String username) {
        return accountService.registerStaff(username);
    }

    @QueryMapping(name = "getAccount")
    public AccountDto getAccount(@Argument(name = "id") String id) {
        UUID uuid = UUID.fromString(id);
        return accountService.getAccountById(uuid);
    }

    @QueryMapping(name = "getListStaff")
    public List<AccountDto> getListStaff() {
        return accountService.getListStaff();
    }

    @MutationMapping(name = "refreshToken")
    public AccountResponse refresh() {
        String refreshToken = request.getHeader("Authorization");
        return accountService.refresh(refreshToken.substring(7));
    }

    @MutationMapping(name = "updateAccount")
    public AccountDto updateAccount(@Argument(name = "id") String id, @Argument(name = "account") AccountUpdateDto accountUpdateDto) {
        UUID uuid = UUID.fromString(id);
        return accountService.updateAccount(uuid, accountUpdateDto);
    }

    @QueryMapping
    public AccountReadDto getAccountByUsername(@Argument(name = "username") String username) {
        return accountService.getAccountByUsername(username);
    }
}
