package com.pagepal.capstone.controllers;

import com.pagepal.capstone.configurations.jwt.JwtService;
import com.pagepal.capstone.dtos.account.*;
import com.pagepal.capstone.dtos.recording.RecordingDto;
import com.pagepal.capstone.services.AccountService;
import com.pagepal.capstone.services.ZoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
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
    private final ZoomService zoomService;
    private final JwtService jwtService;
    private final static String ROLE_CUSTOMER = "CUSTOMER";
    private final static String ROLE_STAFF = "STAFF";

    @MutationMapping(name = "login")
    public AccountResponse login(@Argument(name = "account") AccountRequest accountRequest) {
        return accountService.authenticate(accountRequest);
    }

    @MutationMapping(name = "loginWithGoogle")
    public AccountResponse loginWithGoogle(@Argument(name = "token") String token) {
        return accountService.authenticateWithGoogle(token);
    }

    @MutationMapping
    public AccountReadDto updatePassword(@Argument(name = "id") UUID id, @Argument(name = "password") String password) throws Exception {
        return accountService.updatePassword(id, password);
    }

    @MutationMapping
    public String verifyCode(@Argument(name = "id") UUID id) throws Exception {
        return accountService.verifyCode(id);
    }

    @MutationMapping(name = "verifyEmailRegister")
    public String verifyEmailRegister(@Argument(name = "register") RegisterRequest registerRequest) throws Exception {
        return accountService.verifyEmailRegister(registerRequest);
    }

    @MutationMapping(name = "register")
    public AccountResponse register(@Argument(name = "register") RegisterRequest registerRequest) {
        return accountService.register(registerRequest);
    }

    @MutationMapping(name = "registerStaff")
    public AccountDto registerStaff(@Argument(name = "staff") AccountStaffCreateDto account) {
        return accountService.registerStaff(account);
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

    @QueryMapping(name = "getListCustomer")
    public List<AccountDto> getListCustomer() {
        return accountService.getListCustomer();
    }

    @QueryMapping(name = "getListReader")
    public List<AccountDto> getListReader() {
        return accountService.getListReader();
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

    @MutationMapping(name = "updateAccountState")
    public AccountDto updateAccountState(@Argument(name = "id") UUID id, @Argument(name = "accountState") String accountState) {
        return accountService.updateAccountState(id, accountState);
    }

    @QueryMapping
    public RecordingDto getRecording(){
        return zoomService.getRecording("88446394964");
    }
}
