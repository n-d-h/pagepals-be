package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.configurations.jwt.JwtService;
import com.pagepal.capstone.dtos.account.*;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.LoginTypeEnum;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.AccountMapper;
import com.pagepal.capstone.repositories.*;
import com.pagepal.capstone.services.AccountService;
import com.pagepal.capstone.services.EmailService;
import com.pagepal.capstone.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
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

    private static final String SECRET_KEY = "jkHGs0lbxWwbirSG";
    private final WalletRepository walletRepository;

    private final DateUtils dateUtils;

    String emailBody = """
            Dear %s,
                        
            Thank you for using PagePals! To complete your account verification, please use the following OTP (One-Time Password):
                        
            Verification Code: %s
                        
            This OTP is valid for a single use only and should not be shared with anyone.
                        
            If you did not request this verification code, please ignore this email.
                        
            Please note: This email address is not monitored for replies.
            For any assistance or queries, please contact us at %s.
                        
            Also, don't forget to visit %s to explore more services and features available on PagePals!
            We're constantly updating our offerings to provide you with the best experience.
                        
            Best regards,
            The PagePals Team
            """;

    public static String encodeVerificationCode(String verificationCode) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(verificationCode.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String verifyEmailRegister(RegisterRequest request) throws Exception {

        checkExits(request.getUsername(), request.getEmail());

        String subject = "[PagePals]: OTP Verification Code";
        String website = "https://pagepals-fe.vercel.app";
        String code = generateVerificationCode();
        String body = emailBody.formatted(request.getUsername(), code, website, website);
        emailService.sendSimpleEmail(
                request.getEmail(),
                subject,
                body);

        return encodeVerificationCode(code);
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

        var accountState = accountStateRepository.findByNameAndStatus(ACTIVE, Status.ACTIVE).orElseThrow(
                () -> new RuntimeException("Account State not found")
        );

        var account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setEmail(request.getEmail());
        account.setLoginType(LoginTypeEnum.NORMAL);
        account.setRole(role);
        account.setCreatedAt(dateUtils.getCurrentVietnamDate());
        account.setAccountState(accountState);
        var savedAccount = accountRepository.save(account);
        Customer result = null;
        if (savedAccount != null) {
            Customer customer = new Customer();
            customer.setAccount(savedAccount);
            customer.setFullName(request.getUsername());
            customer.setCreatedAt(dateUtils.getCurrentVietnamDate());
            customer.setStatus(Status.ACTIVE);
            result = customerRepository.save(customer);
        }

        if (result == null) {
            throw new RuntimeException("Create customer failed! Please try again!");
        }

        Wallet wallet = new Wallet();
        wallet.setAccount(savedAccount);
        wallet.setStatus(Status.ACTIVE);
        wallet.setCreatedAt(dateUtils.getCurrentVietnamDate());
        wallet.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        wallet.setTokenAmount(0);
        wallet.setCash((float) 0);
        Wallet resultWallet = walletRepository.save(wallet);

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
        if (account == null) {
            throw new RuntimeException("Account not found");
        }
        if ("DELETED".equals(account.getAccountState().getName())) {
            throw new RuntimeException("Account is deleted");
        }
        if("BANNED".equals(account.getAccountState().getName())){
            throw new RuntimeException("Account is banned");
        }
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
        if(account == null){
            throw new RuntimeException("Account not found");
        }
        if ("DELETED".equals(account.getAccountState().getName())) {
            throw new RuntimeException("Account is deleted");
        }
        if("BANNED".equals(account.getAccountState().getName())){
            throw new RuntimeException("Account is banned");
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
        accountCreate.setCreatedAt(dateUtils.getCurrentVietnamDate());
        AccountState state = accountStateRepository.findByNameAndStatus(ACTIVE, Status.ACTIVE).orElseThrow(
                () -> new RuntimeException("Account State not found")
        );
        accountCreate.setAccountState(state);
        Account account = accountRepository.save(accountCreate);
        if (account != null) {
            Customer cusCreate = new Customer();
            cusCreate.setAccount(account);
            cusCreate.setFullName(accountGoogleDto.getName());
            cusCreate.setCreatedAt(dateUtils.getCurrentVietnamDate());
            cusCreate.setStatus(Status.ACTIVE);
            Customer result = customerRepository.save(cusCreate);
            Wallet wallet = new Wallet();
            wallet.setAccount(account);
            wallet.setStatus(Status.ACTIVE);
            wallet.setCreatedAt(dateUtils.getCurrentVietnamDate());
            wallet.setUpdatedAt(dateUtils.getCurrentVietnamDate());
            wallet.setTokenAmount(0);
            wallet.setCash((float) 0);
            Wallet resultWallet = walletRepository.save(wallet);

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

        checkExits(account.getUsername(), account.getEmail());

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
        accountCreate.setCreatedAt(dateUtils.getCurrentVietnamDate());
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
//        Role role = roleRepository
//                .findByName(ROLE_CUSTOMER)
//                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        List<Account> accounts = accountRepository.findByRoles(Arrays.asList(ROLE_CUSTOMER, ROLE_READER));
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
        account.setUpdatedAt(dateUtils.getCurrentVietnamDate());
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
        account.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        account = accountRepository.save(account);
        return AccountMapper.INSTANCE.toDto(account);
    }

    @Override
    public AccountReadDto updatePassword(UUID id, String password) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Account not found"));

        if (passwordEncoder.encode(password).equals(account.getPassword())) {
            throw new RuntimeException("New password must be different from the old password");
        }

        account.setPassword(passwordEncoder.encode(password));
        account.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        account = accountRepository.save(account);

        return AccountMapper.INSTANCE.toAccountReadDto(account);
    }

    @Override
    public String verifyCode(UUID id) throws Exception {
        Account account = accountRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Account not found"));
        String username = account.getUsername();
        String subject = "[PagePals]: OTP Verification Code";
        String website = "https://pagepals-fe.vercel.app";
        String code = generateVerificationCode();
        String body = emailBody.formatted(username, code, website, website);

        emailService.sendSimpleEmail(
                account.getEmail(),
                subject,
                body);

        return encodeVerificationCode(code);
    }

    @Override
    public AccountDto updateFcmToken(UUID id, String fcmToken, Boolean isWebToken) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new ValidationException("Account not found"));
        if (Objects.equals(isWebToken, Boolean.TRUE)) {
            account.setFcmWebToken(fcmToken);

            List<Account> accounts = accountRepository.findAllByFcmWebToken(fcmToken);
            for (Account acc : accounts) {
                acc.setFcmWebToken(null);
            }
            accountRepository.saveAll(accounts);

        } else {
            account.setFcmMobileToken(fcmToken);

            List<Account> accounts = accountRepository.findAllByFcmMobileToken(fcmToken);
            for (Account acc : accounts) {
                acc.setFcmMobileToken(null);
            }
            accountRepository.saveAll(accounts);
        }
        account.setUpdatedAt(dateUtils.getCurrentVietnamDate());
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
