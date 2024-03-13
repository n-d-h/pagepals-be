package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.configurations.jwt.JwtService;
import com.pagepal.capstone.dtos.account.*;
import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.entities.postgre.AccountState;
import com.pagepal.capstone.entities.postgre.Customer;
import com.pagepal.capstone.entities.postgre.Role;
import com.pagepal.capstone.enums.LoginTypeEnum;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.AccountMapper;
import com.pagepal.capstone.repositories.postgre.AccountRepository;
import com.pagepal.capstone.repositories.postgre.AccountStateRepository;
import com.pagepal.capstone.repositories.postgre.CustomerRepository;
import com.pagepal.capstone.repositories.postgre.RoleRepository;
import com.pagepal.capstone.services.AccountService;
import com.pagepal.capstone.services.EmailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
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

    private final EmailService emailService;
    private final static String ROLE_CUSTOMER = "CUSTOMER";
    private final static String ROLE_STAFF = "STAFF";
    private final static String ROLE_ADMIN = "ADMIN";
    private final static String ROLE_READER = "READER";
    private final static String ACTIVE = "ACTIVE";
    private final CustomerRepository customerRepository;

    public String verifyEmailRegister(RegisterRequest request) {

        checkExits(request.getUsername(), request.getEmail());

        String code = generateVerificationCode();
        emailService.sendSimpleEmail(
                request.getEmail(),
                "Verification Code",
                "Your verification code is: " + code);

        return code;
    }

    private static String generateVerificationCode() {
        Random random = new Random();
        int codeLength = 6; // Length of the verification code
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < codeLength; i++) {
            int digit = random.nextInt(10); // Generate a random digit between 0 and 9
            sb.append(digit);
        }

        return sb.toString();
    }

    public AccountResponse register(RegisterRequest request) {

        checkExits(request.getUsername(), request.getEmail());

        var role = roleRepository.findByName(ROLE_CUSTOMER).orElseThrow(
                () -> new RuntimeException("Role not found")
        );

        var account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setEmail(request.getEmail());
        account.setLoginType(LoginTypeEnum.NORMAL);
        account.setRole(role);
        var savedAccount = accountRepository.save(account);
        Customer result = null;
        if(savedAccount != null) {
            Customer customer = new Customer();
            customer.setAccount(savedAccount);
            customer.setFullName(request.getUsername());
            customer.setCreatedAt(new Date());
            customer.setStatus(Status.ACTIVE);
            result = customerRepository.save(customer);
        }

        if (result == null) {
            throw new RuntimeException("Create customer failed! Please try again!");
        }

        var accessToken = jwtService.generateAccessToken(savedAccount);
        var refreshToken = jwtService.generateRefreshToken(savedAccount);

        return new AccountResponse(accessToken, refreshToken);
    }

    private void checkExits(String username, String email) {
        if (accountRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email is already existed");
        }

        if (accountRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username is already existed");
        }
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

    @Override
    public AccountResponse authenticateWithGoogle(String token) {
        AccountGoogleDto accountGoogleDto = jwtService.parseJwtToken(token);
        Account account = accountRepository
                .findByEmail(accountGoogleDto.getEmail())
                .orElse(null);
        if (account == null) {
            boolean result = createAccountWithGoogle(accountGoogleDto);
            if (!result) {
                throw new RuntimeException("Create account with google failed");
            }
            account = accountRepository.findByEmail(accountGoogleDto.getEmail()).orElse(null);
        }

        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);
        return new AccountResponse(accessToken, refreshToken);
    }

    private boolean createAccountWithGoogle(AccountGoogleDto accountGoogleDto) {
        var role = roleRepository.findByName(ROLE_CUSTOMER).orElseThrow(
                () -> new RuntimeException("Role not found")
        );
        var accountCreate = new Account();
        String username = accountGoogleDto.getEmail().split("@")[0];
        accountCreate.setUsername(username);
        accountCreate.setEmail(accountGoogleDto.getEmail());
        accountCreate.setLoginType(LoginTypeEnum.GOOGLE);
        accountCreate.setRole(role);
        accountCreate.setCreatedAt(new Date());
        AccountState state = accountStateRepository.findByNameAndStatus(ACTIVE, Status.ACTIVE).orElseThrow(
                () -> new RuntimeException("Account State not found")
        );
        accountCreate.setAccountState(state);
        Account account = accountRepository.save(accountCreate);
        if (account != null) {
            Customer cusCreate = new Customer();
            cusCreate.setAccount(account);
            cusCreate.setFullName(accountGoogleDto.getName());
            cusCreate.setCreatedAt(new Date());
            cusCreate.setStatus(Status.ACTIVE);
            Customer result = customerRepository.save(cusCreate);
            return result != null;
        }
        return false;
    }

    public AccountResponse refresh(String token) {
        try {
            String username = jwtService.extractDataFromToken(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userDetails)) {
                String accessToken = jwtService.generateAccessToken(userDetails);
                return new AccountResponse(accessToken, token);
            } else {
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
    public AccountDto registerStaff(AccountStaffCreateDto account) {
        var role = roleRepository.findByName(ROLE_STAFF).orElseThrow(
                () -> new EntityNotFoundException("Role not found")
        );
        var accountCreate = accountRepository
                .findByUsername(account.getUsername())
                .orElse(new Account());

        if (accountCreate.getId() != null) {
            throw new RuntimeException("Username is already existed");
        }

        accountCreate.setUsername(account.getUsername());
        String password = generatePassword();
        accountCreate.setPassword(passwordEncoder.encode(password));
        accountCreate.setLoginType(LoginTypeEnum.NORMAL);
        accountCreate.setRole(role);
        accountCreate.setFullName(account.getFullName());
        accountCreate.setEmail(account.getEmail());
        accountCreate.setPhoneNumber(account.getPhoneNumber());
        accountCreate.setCreatedAt(new Date());
        accountCreate.setAccountState(accountStateRepository.findByNameAndStatus(ACTIVE, Status.ACTIVE).orElseThrow(
                () -> new EntityNotFoundException("Account State not found")
        ));

        var savedAccount = accountRepository.save(accountCreate);
        return AccountMapper.INSTANCE.toDto(savedAccount);
    }

    @Secured("ADMIN")
    @Override
    public List<AccountDto> getListStaff() {
        Role role = roleRepository
                .findByName(ROLE_STAFF)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        List<Account> accounts = accountRepository.findByRole(role);
        return accounts.stream().map(AccountMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Secured({"STAFF", "ADMIN"})
    @Override
    public List<AccountDto> getListCustomer() {
        Role role = roleRepository
                .findByName(ROLE_CUSTOMER)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        List<Account> accounts = accountRepository.findByRole(role);
        return accounts.stream().map(AccountMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Secured({"STAFF", "ADMIN"})
    @Override
    public List<AccountDto> getListReader() {
        Role role = roleRepository
                .findByName(ROLE_READER)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        List<Account> accounts = accountRepository.findByRole(role);
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
        if (accountUpdateDto.getFullName() != null) {
            account.setFullName(accountUpdateDto.getFullName());
        }
        if (accountUpdateDto.getPhoneNumber() != null) {
            account.setPhoneNumber(accountUpdateDto.getPhoneNumber());
        }
        if (accountUpdateDto.getAccountState() != null && !accountUpdateDto.getAccountState().isEmpty()) {
            AccountState state = accountStateRepository
                    .findByNameAndStatus(accountUpdateDto.getAccountState().toUpperCase(), Status.ACTIVE)
                    .orElseThrow(
                            () -> new EntityNotFoundException("Account State not found")
                    );
            account.setAccountState(state);
        }
        account.setUpdatedAt(new Date());
        account = accountRepository.save(account);
        return AccountMapper.INSTANCE.toDto(account);
    }

    @Secured({"CUSTOMER", "READER", "STAFF", "ADMIN"})
    @Override
    public AccountReadDto getAccountByUsername(String username) {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Account not found"));
        return accountRepository.findByUsername(username).map(AccountMapper.INSTANCE::toAccountReadDto)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }

    @Secured({"STAFF", "ADMIN"})
    @Override
    public AccountDto updateAccountState(UUID id, String accountState) {

        Account account = accountRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Account not found"));

        AccountState state = accountStateRepository.findByNameAndStatus(accountState, Status.ACTIVE).orElseThrow(
                () -> new EntityNotFoundException("Account State not found")
        );

        account.setAccountState(state);
        account.setUpdatedAt(new Date());
        account = accountRepository.save(account);
        return AccountMapper.INSTANCE.toDto(account);
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
