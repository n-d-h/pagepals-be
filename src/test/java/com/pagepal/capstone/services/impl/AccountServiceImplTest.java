package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.configurations.jwt.JwtService;
import com.pagepal.capstone.dtos.account.*;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.GenderEnum;
import com.pagepal.capstone.enums.LoginTypeEnum;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.*;
import com.pagepal.capstone.services.EmailService;
import com.pagepal.capstone.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ContextConfiguration(classes = {AccountServiceImpl.class})
@ExtendWith(SpringExtension.class)
public class AccountServiceImplTest {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountStateRepository accountStateRepository;

    @Autowired
    private AccountServiceImpl accountServiceImpl;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private EmailService emailService;

    @MockBean
    private WalletRepository walletRepository;
    @MockBean
    private DateUtils dateUtils;

    //Mock data
    //Account State
    AccountState accountState1 = new AccountState(UUID.randomUUID(), "ACTIVE", Status.ACTIVE, null);
    AccountState accountState2 = new AccountState(UUID.randomUUID(), "INACTIVE", Status.ACTIVE, null);

    //Role
    Role role1 = new Role(UUID.randomUUID(), "READER", Status.ACTIVE, null);
    Role role2 = new Role(UUID.randomUUID(), "CUSTOMER", Status.ACTIVE, null);
    Role role3 = new Role(UUID.randomUUID(), "STAFF", Status.ACTIVE, null);

    //Account
    Account account1 = new Account(UUID.randomUUID(), "username1", "password1", "email1", "fullName1", "0123456789", LoginTypeEnum.NORMAL,
            new Date(), new Date(), new Date(), accountState1, null, null, null, null, role1, null, null);
    Account account2 = new Account(UUID.randomUUID(), "username2", "password2", "email2", "fullName1", "0123456789", LoginTypeEnum.NORMAL,
            new Date(), new Date(), new Date(), accountState2, null, null, null, null, role2, null, null);
    Account account3 = new Account(UUID.randomUUID(), "username3", "password3", "email3", "fullName1", "0123456789", LoginTypeEnum.NORMAL,
            new Date(), new Date(), new Date(), accountState1, null, null, null, null, role3, null, null);
    //Reader
    Reader reader1 = new Reader(UUID.randomUUID(), "name1", 4, "genre1", "Vietnamese"
            , "accent1", "link1", "des1", null,
            null, "vid1","avt",
            new Date(), new Date(), new Date(), Status.ACTIVE, null, null,null, null,
            null, null, null, null,null);
    Customer customer1 = new Customer(UUID.fromString("6ff8f184-e668-4d51-ab18-89ec7d2ba014"), "customer name 1", GenderEnum.MALE, new Date(), "url",
            new Date(), new Date(), new Date(), Status.ACTIVE, account2, new ArrayList<>(), new ArrayList<>());

    /**
     * Method under test: {@link AccountServiceImpl#encodeVerificationCode(String)}
     */
    @Test
    void canEncodeVerificationCode() throws Exception {
        assertEquals("daIp4Kdd0KbKE2PmTj90AIXbfzv2iCK5w4pvUKP6RrM=",
                AccountServiceImpl.encodeVerificationCode("Verification Code"));
    }

    /**
     * Method under test: {@link AccountServiceImpl#verifyEmailRegister(RegisterRequest)}
     */
    @Test
    void canVerifyEmailRegister() throws Exception {
        when(accountRepository.findByEmail((String) any())).thenReturn(Optional.of(new Account()));
        when(accountRepository.findByUsername((String) any())).thenReturn(Optional.of(new Account()));
        assertThrows(RuntimeException.class, () -> accountServiceImpl
                .verifyEmailRegister(new RegisterRequest("jane.doe@example.org", "janedoe", "iloveyou")));
        verify(accountRepository).findByEmail((String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#verifyEmailRegister(RegisterRequest)}
     */
    @Test
    void canVerifyEmailRegister2() throws Exception {
        when(accountRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        when(accountRepository.findByUsername((String) any())).thenReturn(Optional.of(new Account()));
        assertThrows(RuntimeException.class, () -> accountServiceImpl
                .verifyEmailRegister(new RegisterRequest("jane.doe@example.org", "janedoe", "iloveyou")));
        verify(accountRepository).findByEmail((String) any());
        verify(accountRepository).findByUsername((String) any());
    }


    /**
     * Method under test: {@link AccountServiceImpl#verifyEmailRegister(RegisterRequest)}
     */
    @Test
    void canVerifyEmailRegister4() throws Exception {
        when(accountRepository.findByEmail((String) any())).thenThrow(new EntityNotFoundException("An error occurred"));
        when(accountRepository.findByUsername((String) any()))
                .thenThrow(new EntityNotFoundException("An error occurred"));
        assertThrows(EntityNotFoundException.class, () -> accountServiceImpl
                .verifyEmailRegister(new RegisterRequest("jane.doe@example.org", "janedoe", "iloveyou")));
        verify(accountRepository).findByEmail((String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#register(RegisterRequest)}
     */
    @Test
    void canRegister() {
        when(accountRepository.findByEmail((String) any())).thenReturn(Optional.of(new Account()));
        when(accountRepository.findByUsername((String) any())).thenReturn(Optional.of(new Account()));
        assertThrows(RuntimeException.class,
                () -> accountServiceImpl.register(new RegisterRequest("jane.doe@example.org", "janedoe", "iloveyou")));
        verify(accountRepository).findByEmail((String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#register(RegisterRequest)}
     */
    @Test
    void canRegister2() {
        when(accountRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        when(accountRepository.findByUsername((String) any())).thenReturn(Optional.of(new Account()));
        assertThrows(RuntimeException.class,
                () -> accountServiceImpl.register(new RegisterRequest("jane.doe@example.org", "janedoe", "iloveyou")));
        verify(accountRepository).findByEmail((String) any());
        verify(accountRepository).findByUsername((String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#register(RegisterRequest)}
     */
    @Test
    void canRegister4() {
        when(accountRepository.findByEmail((String) any())).thenThrow(new EntityNotFoundException("An error occurred"));
        when(accountRepository.findByUsername((String) any()))
                .thenThrow(new EntityNotFoundException("An error occurred"));
        assertThrows(EntityNotFoundException.class,
                () -> accountServiceImpl.register(new RegisterRequest("jane.doe@example.org", "janedoe", "iloveyou")));
        verify(accountRepository).findByEmail((String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#authenticate(AccountRequest)}
     */
    @Test
    void canAuthenticate() throws AuthenticationException {
        when(accountRepository.findByUsername((String) any())).thenReturn(Optional.of(new Account()));
        when(jwtService.generateAccessToken((UserDetails) any())).thenReturn("ABC123");
        when(jwtService.generateRefreshToken((UserDetails) any())).thenReturn("ABC123");
        when(authenticationManager.authenticate((Authentication) any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        AccountResponse actualAuthenticateResult = accountServiceImpl
                .authenticate(new AccountRequest("janedoe", "iloveyou"));
        assertEquals("ABC123", actualAuthenticateResult.getAccessToken());
        assertEquals("ABC123", actualAuthenticateResult.getRefreshToken());
        verify(accountRepository).findByUsername((String) any());
        verify(jwtService).generateAccessToken((UserDetails) any());
        verify(jwtService).generateRefreshToken((UserDetails) any());
        verify(authenticationManager).authenticate((Authentication) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#authenticate(AccountRequest)}
     */
    @Test
    void canAuthenticate2() throws AuthenticationException {
        when(accountRepository.findByUsername((String) any())).thenReturn(Optional.of(new Account()));
        when(jwtService.generateAccessToken((UserDetails) any())).thenReturn("ABC123");
        when(jwtService.generateRefreshToken((UserDetails) any())).thenReturn("ABC123");
        when(authenticationManager.authenticate((Authentication) any())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class,
                () -> accountServiceImpl.authenticate(new AccountRequest("janedoe", "iloveyou")));
        verify(authenticationManager).authenticate((Authentication) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#authenticateWithGoogle(String)}
     */
    @Test
    void canAuthenticateWithGoogle() {
        when(accountRepository.findByEmail((String) any())).thenReturn(Optional.of(new Account()));
        when(jwtService.generateAccessToken((UserDetails) any())).thenReturn("ABC123");
        when(jwtService.generateRefreshToken((UserDetails) any())).thenReturn("ABC123");
        when(jwtService.parseJwtToken((String) any())).thenReturn(new AccountGoogleDto("Name", "jane.doe@example.org"));
        AccountResponse actualAuthenticateWithGoogleResult = accountServiceImpl.authenticateWithGoogle("ABC123");
        assertEquals("ABC123", actualAuthenticateWithGoogleResult.getAccessToken());
        assertEquals("ABC123", actualAuthenticateWithGoogleResult.getRefreshToken());
        verify(accountRepository).findByEmail((String) any());
        verify(jwtService).parseJwtToken((String) any());
        verify(jwtService).generateAccessToken((UserDetails) any());
        verify(jwtService).generateRefreshToken((UserDetails) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#authenticateWithGoogle(String)}
     */
    @Test
    void canAuthenticateWithGoogle4() {
        when(accountRepository.save((Account) any())).thenReturn(new Account());
        when(accountRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        when(roleRepository.findByName((String) any())).thenReturn(Optional.of(new Role()));
        when(jwtService.generateAccessToken((UserDetails) any())).thenReturn("ABC123");
        when(jwtService.generateRefreshToken((UserDetails) any())).thenReturn("ABC123");
        when(jwtService.parseJwtToken((String) any())).thenReturn(new AccountGoogleDto("Name", "jane.doe@example.org"));
        when(accountStateRepository.findByNameAndStatus((String) any(), (Status) any()))
                .thenReturn(Optional.of(new AccountState()));
        when(customerRepository.save((Customer) any())).thenReturn(new Customer());
        when(walletRepository.save((Wallet) any())).thenReturn(new Wallet());
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        AccountResponse actualAuthenticateWithGoogleResult = accountServiceImpl.authenticateWithGoogle("ABC123");
        assertEquals("ABC123", actualAuthenticateWithGoogleResult.getAccessToken());
        assertEquals("ABC123", actualAuthenticateWithGoogleResult.getRefreshToken());
        verify(accountRepository).save((Account) any());
        verify(accountRepository, atLeast(1)).findByEmail((String) any());
        verify(roleRepository).findByName((String) any());
        verify(jwtService).parseJwtToken((String) any());
        verify(jwtService).generateAccessToken((UserDetails) any());
        verify(jwtService).generateRefreshToken((UserDetails) any());
        verify(accountStateRepository).findByNameAndStatus((String) any(), (Status) any());
        verify(customerRepository).save((Customer) any());
        verify(walletRepository).save((Wallet) any());
        verify(dateUtils, atLeast(1)).getCurrentVietnamDate();
    }

    /**
     * Method under test: {@link AccountServiceImpl#authenticateWithGoogle(String)}
     */
    @Test
    void canAuthenticateWithGoogle6() {
        when(accountRepository.save((Account) any())).thenReturn(null);
        when(accountRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        when(roleRepository.findByName((String) any())).thenReturn(Optional.of(new Role()));
        when(jwtService.generateAccessToken((UserDetails) any())).thenReturn("ABC123");
        when(jwtService.generateRefreshToken((UserDetails) any())).thenReturn("ABC123");
        when(jwtService.parseJwtToken((String) any())).thenReturn(new AccountGoogleDto("Name", "jane.doe@example.org"));
        when(accountStateRepository.findByNameAndStatus((String) any(), (Status) any()))
                .thenReturn(Optional.of(new AccountState()));
        when(customerRepository.save((Customer) any())).thenReturn(new Customer());
        when(walletRepository.save((Wallet) any())).thenReturn(new Wallet());
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assertThrows(RuntimeException.class, () -> accountServiceImpl.authenticateWithGoogle("ABC123"));
        verify(accountRepository).save((Account) any());
        verify(accountRepository).findByEmail((String) any());
        verify(roleRepository).findByName((String) any());
        verify(jwtService).parseJwtToken((String) any());
        verify(accountStateRepository).findByNameAndStatus((String) any(), (Status) any());
        verify(dateUtils).getCurrentVietnamDate();
    }

    /**
     * Method under test: {@link AccountServiceImpl#authenticateWithGoogle(String)}
     */
    @Test
    void canAuthenticateWithGoogle8() {
        when(accountRepository.save((Account) any())).thenReturn(new Account());
        when(accountRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        when(roleRepository.findByName((String) any())).thenReturn(Optional.empty());
        when(jwtService.generateAccessToken((UserDetails) any())).thenReturn("ABC123");
        when(jwtService.generateRefreshToken((UserDetails) any())).thenReturn("ABC123");
        when(jwtService.parseJwtToken((String) any())).thenReturn(new AccountGoogleDto("Name", "jane.doe@example.org"));
        when(accountStateRepository.findByNameAndStatus((String) any(), (Status) any()))
                .thenReturn(Optional.of(new AccountState()));
        when(customerRepository.save((Customer) any())).thenReturn(new Customer());
        when(walletRepository.save((Wallet) any())).thenReturn(new Wallet());
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assertThrows(RuntimeException.class, () -> accountServiceImpl.authenticateWithGoogle("ABC123"));
        verify(accountRepository).findByEmail((String) any());
        verify(roleRepository).findByName((String) any());
        verify(jwtService).parseJwtToken((String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#authenticateWithGoogle(String)}
     */
    @Test
    void canAuthenticateWithGoogle13() {
        when(accountRepository.save((Account) any())).thenReturn(new Account());
        when(accountRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        when(roleRepository.findByName((String) any())).thenReturn(Optional.of(new Role()));
        when(jwtService.generateAccessToken((UserDetails) any())).thenReturn("ABC123");
        when(jwtService.generateRefreshToken((UserDetails) any())).thenReturn("ABC123");
        when(jwtService.parseJwtToken((String) any())).thenReturn(new AccountGoogleDto("Name", "jane.doe@example.org"));
        when(accountStateRepository.findByNameAndStatus((String) any(), (Status) any())).thenReturn(Optional.empty());
        when(customerRepository.save((Customer) any())).thenReturn(new Customer());
        when(walletRepository.save((Wallet) any())).thenReturn(new Wallet());
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assertThrows(RuntimeException.class, () -> accountServiceImpl.authenticateWithGoogle("ABC123"));
        verify(accountRepository).findByEmail((String) any());
        verify(roleRepository).findByName((String) any());
        verify(jwtService).parseJwtToken((String) any());
        verify(accountStateRepository).findByNameAndStatus((String) any(), (Status) any());
        verify(dateUtils).getCurrentVietnamDate();
    }

    /**
     * Method under test: {@link AccountServiceImpl#authenticateWithGoogle(String)}
     */
    @Test
    void canAuthenticateWithGoogle14() {
        when(accountRepository.save((Account) any())).thenReturn(new Account());
        when(accountRepository.findByEmail((String) any())).thenReturn(Optional.empty());
        when(roleRepository.findByName((String) any())).thenReturn(Optional.of(new Role()));
        when(jwtService.generateAccessToken((UserDetails) any())).thenReturn("ABC123");
        when(jwtService.generateRefreshToken((UserDetails) any())).thenReturn("ABC123");
        when(jwtService.parseJwtToken((String) any())).thenReturn(new AccountGoogleDto("Name", "jane.doe@example.org"));
        when(accountStateRepository.findByNameAndStatus((String) any(), (Status) any()))
                .thenReturn(Optional.of(new AccountState()));
        when(customerRepository.save((Customer) any())).thenReturn(null);
        when(walletRepository.save((Wallet) any())).thenReturn(new Wallet());
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assertThrows(RuntimeException.class, () -> accountServiceImpl.authenticateWithGoogle("ABC123"));
        verify(accountRepository).save((Account) any());
        verify(accountRepository).findByEmail((String) any());
        verify(roleRepository).findByName((String) any());
        verify(jwtService).parseJwtToken((String) any());
        verify(accountStateRepository).findByNameAndStatus((String) any(), (Status) any());
        verify(customerRepository).save((Customer) any());
        verify(walletRepository).save((Wallet) any());
        verify(dateUtils, atLeast(1)).getCurrentVietnamDate();
    }

    /**
     * Method under test: {@link AccountServiceImpl#refresh(String)}
     */
    @Test
    void canRefresh() throws UsernameNotFoundException {
        when(jwtService.generateAccessToken((UserDetails) any())).thenReturn("ABC123");
        when(jwtService.isTokenValid((String) any(), (UserDetails) any())).thenReturn(true);
        when(jwtService.extractDataFromToken((String) any())).thenReturn("ABC123");
        when(userDetailsService.loadUserByUsername((String) any())).thenReturn(new Account());
        AccountResponse actualRefreshResult = accountServiceImpl.refresh("ABC123");
        assertEquals("ABC123", actualRefreshResult.getAccessToken());
        assertEquals("ABC123", actualRefreshResult.getRefreshToken());
        verify(jwtService).isTokenValid((String) any(), (UserDetails) any());
        verify(jwtService).extractDataFromToken((String) any());
        verify(jwtService).generateAccessToken((UserDetails) any());
        verify(userDetailsService).loadUserByUsername((String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#refresh(String)}
     */
    @Test
    void canRefresh2() throws UsernameNotFoundException {
        when(jwtService.generateAccessToken((UserDetails) any())).thenReturn("ABC123");
        when(jwtService.isTokenValid((String) any(), (UserDetails) any())).thenReturn(true);
        when(jwtService.extractDataFromToken((String) any())).thenReturn("ABC123");
        when(userDetailsService.loadUserByUsername((String) any())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> accountServiceImpl.refresh("ABC123"));
        verify(jwtService).extractDataFromToken((String) any());
        verify(userDetailsService).loadUserByUsername((String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#refresh(String)}
     */
    @Test
    void canRefresh3() throws UsernameNotFoundException {
        when(jwtService.generateAccessToken((UserDetails) any())).thenReturn("ABC123");
        when(jwtService.isTokenValid((String) any(), (UserDetails) any())).thenReturn(false);
        when(jwtService.extractDataFromToken((String) any())).thenReturn("ABC123");
        when(userDetailsService.loadUserByUsername((String) any())).thenReturn(new Account());
        assertThrows(RuntimeException.class, () -> accountServiceImpl.refresh("ABC123"));
        verify(jwtService).isTokenValid((String) any(), (UserDetails) any());
        verify(jwtService).extractDataFromToken((String) any());
        verify(userDetailsService).loadUserByUsername((String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#getAccountById(UUID)}
     */
    @Test
    void canGetAccountById() {
        when(accountRepository.findById((UUID) any())).thenReturn(Optional.of(new Account()));
        AccountDto actualAccountById = accountServiceImpl.getAccountById(UUID.randomUUID());
        assertNull(actualAccountById.getAccountState());
        assertNull(actualAccountById.getWallet());
        assertNull(actualAccountById.getUsername());
        assertNull(actualAccountById.getRole());
        assertNull(actualAccountById.getReader());
        assertNull(actualAccountById.getPhoneNumber());
        assertNull(actualAccountById.getPassword());
        assertNull(actualAccountById.getLoginType());
        assertNull(actualAccountById.getId());
        assertNull(actualAccountById.getFullName());
        assertNull(actualAccountById.getEmail());
        assertNull(actualAccountById.getDeletedAt());
        assertNull(actualAccountById.getCustomer());
        verify(accountRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#getListStaff()}
     */
    @Test
    void canGetListStaff() {
        when(accountRepository.findByRole((Role) any())).thenReturn(new ArrayList<>());
        when(roleRepository.findByName((String) any())).thenReturn(Optional.of(new Role()));
        assertTrue(accountServiceImpl.getListStaff().isEmpty());
        verify(accountRepository).findByRole((Role) any());
        verify(roleRepository).findByName((String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#getListStaff()}
     */
    @Test
    void canGetListStaff2() {
        when(accountRepository.findByRole((Role) any())).thenThrow(new RuntimeException());
        when(roleRepository.findByName((String) any())).thenReturn(Optional.of(new Role()));
        assertThrows(RuntimeException.class, () -> accountServiceImpl.getListStaff());
        verify(accountRepository).findByRole((Role) any());
        verify(roleRepository).findByName((String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#getListStaff()}
     */
    @Test
    void canGetListStaff3() {
        ArrayList<Account> accountList = new ArrayList<>();
        accountList.add(new Account());
        when(accountRepository.findByRole((Role) any())).thenReturn(accountList);
        when(roleRepository.findByName((String) any())).thenReturn(Optional.of(new Role()));
        assertEquals(1, accountServiceImpl.getListStaff().size());
        verify(accountRepository).findByRole((Role) any());
        verify(roleRepository).findByName((String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#getListStaff()}
     */
    @Test
    void canGetListStaff4() {
        when(accountRepository.findByRole((Role) any())).thenReturn(new ArrayList<>());
        when(roleRepository.findByName((String) any())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> accountServiceImpl.getListStaff());
        verify(roleRepository).findByName((String) any());
    }


    /**
     * Method under test: {@link AccountServiceImpl#getListCustomer()}
     */
    @Test
    void canGetListCustomer() {
        when(accountRepository.findByRoles((List<String>) any())).thenReturn(new ArrayList<>());
        assertTrue(accountServiceImpl.getListCustomer().isEmpty());
        verify(accountRepository).findByRoles((List<String>) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#getListCustomer()}
     */
    @Test
    void canGetListCustomer2() {
        ArrayList<Account> accountList = new ArrayList<>();
        accountList.add(new Account());
        when(accountRepository.findByRoles((List<String>) any())).thenReturn(accountList);
        assertEquals(1, accountServiceImpl.getListCustomer().size());
        verify(accountRepository).findByRoles((List<String>) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#getListCustomer()}
     */
    @Test
    void canGetListCustomer3() {
        when(accountRepository.findByRoles((List<String>) any())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> accountServiceImpl.getListCustomer());
        verify(accountRepository).findByRoles((List<String>) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#getListReader()}
     */
    @Test
    void canGetListReader() {
        when(accountRepository.findByRole((Role) any())).thenReturn(new ArrayList<>());
        when(roleRepository.findByName((String) any())).thenReturn(Optional.of(new Role()));
        assertTrue(accountServiceImpl.getListReader().isEmpty());
        verify(accountRepository).findByRole((Role) any());
        verify(roleRepository).findByName((String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#getListReader()}
     */
    @Test
    void canGetListReader2() {
        when(accountRepository.findByRole((Role) any())).thenThrow(new RuntimeException());
        when(roleRepository.findByName((String) any())).thenReturn(Optional.of(new Role()));
        assertThrows(RuntimeException.class, () -> accountServiceImpl.getListReader());
        verify(accountRepository).findByRole((Role) any());
        verify(roleRepository).findByName((String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#getListReader()}
     */
    @Test
    void canGetListReader3() {
        ArrayList<Account> accountList = new ArrayList<>();
        accountList.add(new Account());
        when(accountRepository.findByRole((Role) any())).thenReturn(accountList);
        when(roleRepository.findByName((String) any())).thenReturn(Optional.of(new Role()));
        assertEquals(1, accountServiceImpl.getListReader().size());
        verify(accountRepository).findByRole((Role) any());
        verify(roleRepository).findByName((String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#getListReader()}
     */
    @Test
    void canGetListReader4() {
        when(accountRepository.findByRole((Role) any())).thenReturn(new ArrayList<>());
        when(roleRepository.findByName((String) any())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> accountServiceImpl.getListReader());
        verify(roleRepository).findByName((String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#updateAccount(UUID, AccountUpdateDto)}
     */
    @Test
    void canUpdateAccount() {
        when(accountRepository.save(any())).thenReturn(new Account());
        when(accountRepository.findById(any())).thenReturn(Optional.of(new Account()));
        when(accountStateRepository.findByNameAndStatus(any(), any())).thenReturn(Optional.of(new AccountState()));
        when(passwordEncoder.encode(any())).thenReturn("secret");
        UUID id = UUID.randomUUID();
        AccountDto actualUpdateAccountResult = accountServiceImpl.updateAccount(id,
                new AccountUpdateDto("janedoe", "iloveyou", "jane.doe@example.org", "fullName1", "0123456789", "ACTIVE"));
        assertNull(actualUpdateAccountResult.getUsername());
        assertNull(actualUpdateAccountResult.getPassword());
        assertNull(actualUpdateAccountResult.getLoginType());
        assertNull(actualUpdateAccountResult.getId());
        assertNull(actualUpdateAccountResult.getEmail());
        assertNull(actualUpdateAccountResult.getDeletedAt());
        verify(accountRepository).save(any());
        verify(accountRepository).findById(any());
        verify(passwordEncoder).encode(any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#updateAccount(UUID, AccountUpdateDto)}
     */
    @Test
    void shouldThrowWhenIdNotFound() {
        when(accountRepository.save(any())).thenReturn(mock(Account.class));
        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(any())).thenReturn("secret");
        UUID id = UUID.randomUUID();
        assertThrows(EntityNotFoundException.class, () -> accountServiceImpl.updateAccount(id,
                new AccountUpdateDto("janedoe", "iloveyou", "jane.doe@example.org", "fullName1", "0123456789", "ACTIVE")));
        verify(accountRepository).findById(any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#updateAccount(UUID, AccountUpdateDto)}
     */
    @Test
    void canUpdateAccount1() {
        Account account = mock(Account.class);
        doThrow(new EntityNotFoundException()).when(account).setEmail(any());
        doThrow(new EntityNotFoundException()).when(account).setPassword(any());
        doThrow(new EntityNotFoundException()).when(account).setUsername(any());
        Optional<Account> ofResult = Optional.of(account);
        Account account1 = mock(Account.class);
        when(account1.getLoginType()).thenReturn(LoginTypeEnum.NORMAL);
        when(account1.getEmail()).thenReturn("jane.doe@example.org");
        when(account1.getPassword()).thenReturn("iloveyou");
        when(account1.getUsername()).thenReturn("janedoe");
        Date fromResult = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        when(account1.getCreatedAt()).thenReturn(fromResult);
        Date fromResult1 = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        when(account1.getDeletedAt()).thenReturn(fromResult1);
        Date fromResult2 = Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant());
        when(account1.getUpdatedAt()).thenReturn(fromResult2);
        UUID randomUUIDResult = UUID.randomUUID();
        when(account1.getId()).thenReturn(randomUUIDResult);
        when(accountRepository.save(any())).thenReturn(account1);
        when(accountRepository.findById(any())).thenReturn(ofResult);
        when(passwordEncoder.encode(any())).thenReturn("secret");
        UUID id = UUID.randomUUID();
        AccountDto actualUpdateAccountResult = accountServiceImpl.updateAccount(id, new AccountUpdateDto());
        assertEquals("janedoe", actualUpdateAccountResult.getUsername());
        assertEquals("iloveyou", actualUpdateAccountResult.getPassword());
        assertEquals(LoginTypeEnum.NORMAL, actualUpdateAccountResult.getLoginType());
        assertSame(randomUUIDResult, actualUpdateAccountResult.getId());
        assertEquals("jane.doe@example.org", actualUpdateAccountResult.getEmail());
        verify(accountRepository).save(any());
        verify(accountRepository).findById(any());
        verify(account1).getLoginType();
        verify(account1).getEmail();
        verify(account1).getPassword();
        verify(account1).getUsername();
        verify(account1).getCreatedAt();
        verify(account1).getDeletedAt();
        verify(account1).getUpdatedAt();
        verify(account1).getId();
    }

    /**
     * Method under test: {@link AccountServiceImpl#updateAccount(UUID, AccountUpdateDto)}
     */
    @Test
    void canUpdateAccount2() {
        when(accountRepository.save((Account) any())).thenReturn(new Account());
        when(accountRepository.findById((UUID) any())).thenReturn(Optional.of(new Account()));
        when(accountStateRepository.findByNameAndStatus((String) any(), (Status) any()))
                .thenReturn(Optional.of(new AccountState()));
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        when(passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        UUID id = UUID.randomUUID();
        AccountDto actualUpdateAccountResult = accountServiceImpl.updateAccount(id,
                new AccountUpdateDto("janedoe", "iloveyou", "jane.doe@example.org", "Dr Jane Doe", "6625550144", "3"));
        assertNull(actualUpdateAccountResult.getAccountState());
        assertNull(actualUpdateAccountResult.getWallet());
        assertNull(actualUpdateAccountResult.getUsername());
        assertNull(actualUpdateAccountResult.getRole());
        assertNull(actualUpdateAccountResult.getReader());
        assertNull(actualUpdateAccountResult.getPhoneNumber());
        assertNull(actualUpdateAccountResult.getPassword());
        assertNull(actualUpdateAccountResult.getLoginType());
        assertNull(actualUpdateAccountResult.getId());
        assertNull(actualUpdateAccountResult.getFullName());
        assertNull(actualUpdateAccountResult.getEmail());
        assertNull(actualUpdateAccountResult.getDeletedAt());
        assertNull(actualUpdateAccountResult.getCustomer());
        verify(accountRepository).save((Account) any());
        verify(accountRepository).findById((UUID) any());
        verify(accountStateRepository).findByNameAndStatus((String) any(), (Status) any());
        verify(dateUtils).getCurrentVietnamDate();
        verify(passwordEncoder).encode((CharSequence) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#updateAccount(UUID, AccountUpdateDto)}
     */
    @Test
    void canUpdateAccount3() {
        when(accountRepository.save((Account) any())).thenReturn(new Account());
        when(accountRepository.findById((UUID) any())).thenReturn(Optional.of(new Account()));
        when(accountStateRepository.findByNameAndStatus((String) any(), (Status) any()))
                .thenReturn(Optional.of(new AccountState()));
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        when(passwordEncoder.encode((CharSequence) any())).thenThrow(new RuntimeException());
        UUID id = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> accountServiceImpl.updateAccount(id,
                new AccountUpdateDto("janedoe", "iloveyou", "jane.doe@example.org", "Dr Jane Doe", "6625550144", "3")));
        verify(accountRepository).findById((UUID) any());
        verify(passwordEncoder).encode((CharSequence) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#updateAccount(UUID, AccountUpdateDto)}
     */
    @Test
    void canUpdateAccount4() {
        when(accountRepository.save((Account) any())).thenReturn(null);
        when(accountRepository.findById((UUID) any())).thenReturn(Optional.of(new Account()));
        when(accountStateRepository.findByNameAndStatus((String) any(), (Status) any()))
                .thenReturn(Optional.of(new AccountState()));
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        when(passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        UUID id = UUID.randomUUID();
        assertNull(accountServiceImpl.updateAccount(id,
                new AccountUpdateDto("janedoe", "iloveyou", "jane.doe@example.org", "Dr Jane Doe", "6625550144", "3")));
        verify(accountRepository).save((Account) any());
        verify(accountRepository).findById((UUID) any());
        verify(accountStateRepository).findByNameAndStatus((String) any(), (Status) any());
        verify(dateUtils).getCurrentVietnamDate();
        verify(passwordEncoder).encode((CharSequence) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#getAccountByUsername(String)}
     */
    @Test
    void canGetAccountByUsername() {
        when(accountRepository.findByUsername((String) any())).thenReturn(Optional.of(new Account()));
        AccountReadDto actualAccountByUsername = accountServiceImpl.getAccountByUsername("janedoe");
        assertNull(actualAccountByUsername.getAccountState());
        assertNull(actualAccountByUsername.getWallet());
        assertNull(actualAccountByUsername.getUsername());
        assertNull(actualAccountByUsername.getRole());
        assertNull(actualAccountByUsername.getReader());
        assertNull(actualAccountByUsername.getPhoneNumber());
        assertNull(actualAccountByUsername.getLoginType());
        assertNull(actualAccountByUsername.getId());
        assertNull(actualAccountByUsername.getFullName());
        assertNull(actualAccountByUsername.getEmail());
        assertNull(actualAccountByUsername.getCustomer());
        verify(accountRepository, atLeast(1)).findByUsername((String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#updateAccountState(UUID, String)}
     */
    @Test
    void canUpdateAccountState() {
        when(accountRepository.save((Account) any())).thenReturn(new Account());
        when(accountRepository.findById((UUID) any())).thenReturn(Optional.of(new Account()));
        when(accountStateRepository.findByNameAndStatus((String) any(), (Status) any()))
                .thenReturn(Optional.of(new AccountState()));
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        AccountDto actualUpdateAccountStateResult = accountServiceImpl.updateAccountState(UUID.randomUUID(), "3");
        assertNull(actualUpdateAccountStateResult.getAccountState());
        assertNull(actualUpdateAccountStateResult.getWallet());
        assertNull(actualUpdateAccountStateResult.getUsername());
        assertNull(actualUpdateAccountStateResult.getRole());
        assertNull(actualUpdateAccountStateResult.getReader());
        assertNull(actualUpdateAccountStateResult.getPhoneNumber());
        assertNull(actualUpdateAccountStateResult.getPassword());
        assertNull(actualUpdateAccountStateResult.getLoginType());
        assertNull(actualUpdateAccountStateResult.getId());
        assertNull(actualUpdateAccountStateResult.getFullName());
        assertNull(actualUpdateAccountStateResult.getEmail());
        assertNull(actualUpdateAccountStateResult.getDeletedAt());
        assertNull(actualUpdateAccountStateResult.getCustomer());
        verify(accountRepository).save((Account) any());
        verify(accountRepository).findById((UUID) any());
        verify(accountStateRepository).findByNameAndStatus((String) any(), (Status) any());
        verify(dateUtils).getCurrentVietnamDate();
    }

    /**
     * Method under test: {@link AccountServiceImpl#updateAccountState(UUID, String)}
     */
    @Test
    void canUpdateAccountState3() {
        when(accountRepository.save((Account) any())).thenReturn(null);
        when(accountRepository.findById((UUID) any())).thenReturn(Optional.of(new Account()));
        when(accountStateRepository.findByNameAndStatus((String) any(), (Status) any()))
                .thenReturn(Optional.of(new AccountState()));
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assertNull(accountServiceImpl.updateAccountState(UUID.randomUUID(), "3"));
        verify(accountRepository).save((Account) any());
        verify(accountRepository).findById((UUID) any());
        verify(accountStateRepository).findByNameAndStatus((String) any(), (Status) any());
        verify(dateUtils).getCurrentVietnamDate();
    }

    /**
     * Method under test: {@link AccountServiceImpl#updatePassword(UUID, String)}
     */
    @Test
    void canUpdatePassword() {
        when(accountRepository.save((Account) any())).thenReturn(new Account());
        when(accountRepository.findById((UUID) any())).thenReturn(Optional.of(new Account()));
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        when(passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        AccountReadDto actualUpdatePasswordResult = accountServiceImpl.updatePassword(UUID.randomUUID(), "iloveyou");
        assertNull(actualUpdatePasswordResult.getAccountState());
        assertNull(actualUpdatePasswordResult.getWallet());
        assertNull(actualUpdatePasswordResult.getUsername());
        assertNull(actualUpdatePasswordResult.getRole());
        assertNull(actualUpdatePasswordResult.getReader());
        assertNull(actualUpdatePasswordResult.getPhoneNumber());
        assertNull(actualUpdatePasswordResult.getLoginType());
        assertNull(actualUpdatePasswordResult.getId());
        assertNull(actualUpdatePasswordResult.getFullName());
        assertNull(actualUpdatePasswordResult.getEmail());
        assertNull(actualUpdatePasswordResult.getCustomer());
        verify(accountRepository).save((Account) any());
        verify(accountRepository).findById((UUID) any());
        verify(dateUtils).getCurrentVietnamDate();
        verify(passwordEncoder, atLeast(1)).encode((CharSequence) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#verifyCode(UUID)}
     */
    @Test
    void canVerifyCode() throws Exception {
        when(accountRepository.findById((UUID) any())).thenReturn(Optional.of(new Account()));
        doNothing().when(emailService).sendSimpleEmail((String) any(), (String) any(), (String) any());
        accountServiceImpl.verifyCode(UUID.randomUUID());
        verify(accountRepository).findById((UUID) any());
        verify(emailService).sendSimpleEmail((String) any(), (String) any(), (String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#verifyCode(UUID)}
     */
    @Test
    void canVerifyCode2() throws Exception {
        when(accountRepository.findById((UUID) any())).thenReturn(Optional.of(new Account()));
        doThrow(new RuntimeException()).when(emailService)
                .sendSimpleEmail((String) any(), (String) any(), (String) any());
        assertThrows(RuntimeException.class, () -> accountServiceImpl.verifyCode(UUID.randomUUID()));
        verify(accountRepository).findById((UUID) any());
        verify(emailService).sendSimpleEmail((String) any(), (String) any(), (String) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#verifyCode(UUID)}
     */
    @Test
    void canVerifyCode4() throws Exception {
        when(accountRepository.findById((UUID) any())).thenReturn(Optional.empty());
        new EntityNotFoundException("An error occurred");
        new EntityNotFoundException("An error occurred");
        doNothing().when(emailService).sendSimpleEmail((String) any(), (String) any(), (String) any());
        assertThrows(EntityNotFoundException.class, () -> accountServiceImpl.verifyCode(UUID.randomUUID()));
        verify(accountRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#updateFcmToken(UUID, String, Boolean)}
     */
    @Test
    void canUpdateFcmToken() {
        when(accountRepository.save((Account) any())).thenReturn(new Account());
        when(accountRepository.findById((UUID) any())).thenReturn(Optional.of(new Account()));
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        AccountDto actualUpdateFcmTokenResult = accountServiceImpl.updateFcmToken(UUID.randomUUID(), "ABC123", true);
        assertNull(actualUpdateFcmTokenResult.getAccountState());
        assertNull(actualUpdateFcmTokenResult.getWallet());
        assertNull(actualUpdateFcmTokenResult.getUsername());
        assertNull(actualUpdateFcmTokenResult.getRole());
        assertNull(actualUpdateFcmTokenResult.getReader());
        assertNull(actualUpdateFcmTokenResult.getPhoneNumber());
        assertNull(actualUpdateFcmTokenResult.getPassword());
        assertNull(actualUpdateFcmTokenResult.getLoginType());
        assertNull(actualUpdateFcmTokenResult.getId());
        assertNull(actualUpdateFcmTokenResult.getFullName());
        assertNull(actualUpdateFcmTokenResult.getEmail());
        assertNull(actualUpdateFcmTokenResult.getDeletedAt());
        assertNull(actualUpdateFcmTokenResult.getCustomer());
        verify(accountRepository).save((Account) any());
        verify(accountRepository).findById((UUID) any());
        verify(dateUtils).getCurrentVietnamDate();
    }

    /**
     * Method under test: {@link AccountServiceImpl#updateFcmToken(UUID, String, Boolean)}
     */
    @Test
    void canUpdateFcmToken3() {
        when(accountRepository.save((Account) any())).thenReturn(null);
        when(accountRepository.findById((UUID) any())).thenReturn(Optional.of(new Account()));
        when(dateUtils.getCurrentVietnamDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        assertNull(accountServiceImpl.updateFcmToken(UUID.randomUUID(), "ABC123", true));
        verify(accountRepository).save((Account) any());
        verify(accountRepository).findById((UUID) any());
        verify(dateUtils).getCurrentVietnamDate();
    }

}
