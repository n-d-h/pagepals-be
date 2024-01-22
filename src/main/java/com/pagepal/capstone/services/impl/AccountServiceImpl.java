package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.configurations.jwt.JwtService;
import com.pagepal.capstone.dtos.account.*;
import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.entities.postgre.AccountState;
import com.pagepal.capstone.entities.postgre.Role;
import com.pagepal.capstone.enums.LoginTypeEnum;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.AccountMapper;
import com.pagepal.capstone.repositories.postgre.AccountRepository;
import com.pagepal.capstone.repositories.postgre.AccountStateRepository;
import com.pagepal.capstone.repositories.postgre.RoleRepository;
import com.pagepal.capstone.services.AccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AccountStateRepository accountStateRepository;
    private final UserDetailsService userDetailsService;
    private final static String ROLE_CUSTOMER = "CUSTOMER";
    private final static String ROLE_STAFF = "STAFF";
    private final static String ACTIVE = "ACTIVE";
    private final AccountMapper accountMapper;

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

    public AccountResponse refresh(String token) {
        try {
            String username = jwtService.extractDataFromToken(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userDetails)) {
                String accessToken = jwtService.generateAccessToken(userDetails);
                return new AccountResponse(accessToken, token);
            }else{
                throw new RuntimeException("Refresh token is invalid");
            }

        } catch (Exception e) {
            throw new RuntimeException("Refresh token is invalid");
        }
    }

    @Override
    public AccountDto getAccountById(UUID id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.map(AccountMapper.INSTANCE::toDto).orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }

    @Secured("ADMIN")
    @Override
    public AccountStaffResponse registerStaff(String username) {
        var role = roleRepository.findByName(ROLE_STAFF).orElseThrow(
                () -> new EntityNotFoundException("Role not found")
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

    @Secured("ADMIN")
    @Override
    public List<AccountDto> getListStaff() {
        AccountState accountState = accountStateRepository
                .findByNameAndStatus(ACTIVE, Status.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Account State not found"));
        Role role = roleRepository
                .findByName(ROLE_STAFF)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        List<Account> accounts = accountRepository.findByAccountStateAndRole(accountState, role);
        return accounts.stream().map(AccountMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Secured({"CUSTOMER", "READER", "ADMIN"})
    @Override
    public AccountDto updateAccount(UUID id, AccountUpdateDto accountUpdateDto) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Account not found"));

        if (accountUpdateDto.getUsername() != null) {
            account.setUsername(accountUpdateDto.getUsername());
        }
        if (accountUpdateDto.getPassword() != null && !accountUpdateDto.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(accountUpdateDto.getPassword()));
        }
        if (accountUpdateDto.getEmail() != null) {
            account.setEmail(accountUpdateDto.getEmail());
        }
        account = accountRepository.save(account);
        return AccountMapper.INSTANCE.toDto(account);
    }

    @Secured({"CUSTOMER", "READER","STAFF","ADMIN"})
    @Override
    public AccountReadDto getAccountByUsername(String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Account not found"));
        System.out.println(account.getCustomer().getFullName());
        return accountRepository.findByUsername(username).map(accountMapper::toAccountReadDto)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
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
