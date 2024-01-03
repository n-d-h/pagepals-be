package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.configurations.jwt.JwtService;
import com.pagepal.capstone.dtos.account.*;
import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.entities.postgre.Role;
import com.pagepal.capstone.enums.LoginTypeEnum;
import com.pagepal.capstone.mappers.AccountMapper;
import com.pagepal.capstone.repositories.postgre.AccountRepository;
import com.pagepal.capstone.repositories.postgre.RoleRepository;
import com.pagepal.capstone.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final static String ROLE_CUSTOMER = "CUSTOMER";
    private final static String ROLE_STAFF = "STAFF";

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

    @Override
    public AccountDto getAccountById(UUID id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.map(AccountMapper.INSTANCE::toDto).orElse(null);
    }

    @Override
    public AccountStaffResponse registerStaff(String username) {
        var role = roleRepository.findByName(ROLE_STAFF).orElseThrow(
                () -> new RuntimeException("Role not found")
        );
        var account = new Account();
        account.setUsername(username);
        String password = generatePassword();
        account.setPassword(passwordEncoder.encode(password));
        account.setLoginType(LoginTypeEnum.NORMAL);
        account.setRole(role);
        var savedAccount = accountRepository.save(account);

        var accessToken = jwtService.generateAccessToken(savedAccount);
        var refreshToken = jwtService.generateRefreshToken(savedAccount);

        return new AccountStaffResponse(username, password, accessToken, refreshToken);
    }

    private String generatePassword() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}
