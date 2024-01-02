package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.configurations.jwt.JwtService;
import com.pagepal.capstone.dtos.account.AccountRequest;
import com.pagepal.capstone.dtos.account.AccountResponse;
import com.pagepal.capstone.dtos.account.RefreshTokenRequest;
import com.pagepal.capstone.dtos.account.RegisterRequest;
import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.entities.postgre.Role;
import com.pagepal.capstone.enums.LoginTypeEnum;
import com.pagepal.capstone.repositories.postgre.AccountRepository;
import com.pagepal.capstone.repositories.postgre.RoleRepository;
import com.pagepal.capstone.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final static String ROLE_CUSTOMER = "CUSTOMER";

    public AccountResponse register(RegisterRequest request) {
        var role = roleRepository.findByName(ROLE_CUSTOMER).orElseThrow(
                () -> new RuntimeException("Role not found")
        );
        var account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setLoginType(LoginTypeEnum.NORMAL);
        account.setRole(role);
        var savedAccount = accountRepository.save(account);

        var accessToken = jwtService.generateAccessToken(savedAccount);
        var refreshToken = jwtService.generateRefreshToken(savedAccount);

        return new AccountResponse(accessToken, refreshToken);
    }

    public AccountResponse authenticate(AccountRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var account = accountRepository.findByUsername(request.getUsername()).orElse(null);

        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);

        return new AccountResponse(accessToken, refreshToken);
    }

    public AccountResponse refresh(RefreshTokenRequest request) {
        try {
            String username = jwtService.extractDataFromToken(request.getRefreshToken());

            Account account = accountRepository.findByUsername(username).orElseThrow(
                    () -> new RuntimeException("User not found")
            );

            String accessToken = jwtService.generateAccessToken(account);
            String refreshToken = jwtService.generateRefreshToken(account);

            return new AccountResponse(accessToken, refreshToken);
        } catch (Exception e) {
            throw new RuntimeException("Refresh token is invalid");
        }
    }
}
