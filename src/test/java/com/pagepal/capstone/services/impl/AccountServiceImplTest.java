package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.configurations.jwt.JwtService;
import com.pagepal.capstone.dtos.account.AccountDto;
import com.pagepal.capstone.dtos.account.AccountUpdateDto;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.enums.GenderEnum;
import com.pagepal.capstone.enums.LoginTypeEnum;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.postgre.AccountRepository;
import com.pagepal.capstone.repositories.postgre.AccountStateRepository;
import com.pagepal.capstone.repositories.postgre.RoleRepository;

import java.time.LocalDate;
import java.time.ZoneOffset;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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
    private AuthenticationManager authenticationManager;

    //Mock data
    //Account State
    AccountState accountState1 = new AccountState(UUID.randomUUID(), "ACTIVE", Status.ACTIVE, null);
    AccountState accountState2 = new AccountState(UUID.randomUUID(), "INACTIVE", Status.ACTIVE, null);

    //Role
    Role role1 = new Role(UUID.randomUUID(), "READER", Status.ACTIVE, null);
    Role role2 = new Role(UUID.randomUUID(), "CUSTOMER", Status.ACTIVE, null);
    Role role3 = new Role(UUID.randomUUID(), "STAFF", Status.ACTIVE, null);

    //Account
    Account account1 = new Account(UUID.randomUUID(), "username1", "password1", "email1", LoginTypeEnum.NORMAL,
            new Date(), new Date(), new Date(), accountState1, null, null, role1, null);
    Account account2 = new Account(UUID.randomUUID(), "username2", "password2", "email2", LoginTypeEnum.NORMAL,
            new Date(), new Date(), new Date(), accountState2, null, null, role2, null);
    Account account3 = new Account(UUID.randomUUID(), "username3", "password3", "email3", LoginTypeEnum.NORMAL,
            new Date(), new Date(), new Date(), accountState1, null, null, role3, null);
    //Reader
    Reader reader1 = new Reader(UUID.randomUUID(), "name1", 5, "genre1", "Vietnamese", "accent1",
            "url", "des1", "123", "123", "url", 123.2, "tag",
            new Date(), new Date(), new Date(), null, account1, null, null, null, null,
            null, null, null);
    Customer customer1 = new Customer(UUID.fromString("6ff8f184-e668-4d51-ab18-89ec7d2ba014"), "customer name 1", GenderEnum.MALE, new Date(), "url",
            new Date(), new Date(), new Date(), Status.ACTIVE, account2, null, null);

    @Test
    void shouldCreateStaffAndGetListStaff() {
        when(accountRepository.save(account3)).thenReturn(account3);
        when(accountStateRepository.findByNameAndStatus("ACTIVE", Status.ACTIVE)).thenReturn(java.util.Optional.of(accountState1));
        when(accountRepository.findByUsername(account3.getUsername())).thenReturn(java.util.Optional.of(account3));
        when(roleRepository.findByName("STAFF")).thenReturn(java.util.Optional.of(role3));
        when(accountRepository.findByAccountStateAndRole(accountState1, role3)).thenReturn(java.util.List.of(account3));

        var actual = accountServiceImpl.registerStaff(account3.getUsername());

        assert actual != null;

        var listStaff = accountServiceImpl.getListStaff();

        assert listStaff.size() > 0;
    }

    /**
     * Method under test: {@link AccountServiceImpl#updateAccount(UUID, AccountUpdateDto)}
     */
    @Test
    void canUpdateAccount() {
        when(accountRepository.save((Account) any())).thenReturn(new Account());
        when(accountRepository.findById((UUID) any())).thenReturn(Optional.of(new Account()));
        when(passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        UUID id = UUID.randomUUID();
        AccountDto actualUpdateAccountResult = accountServiceImpl.updateAccount(id,
                new AccountUpdateDto("janedoe", "iloveyou", "jane.doe@example.org"));
        assertNull(actualUpdateAccountResult.getUsername());
        assertNull(actualUpdateAccountResult.getPassword());
        assertNull(actualUpdateAccountResult.getLoginType());
        assertNull(actualUpdateAccountResult.getId());
        assertNull(actualUpdateAccountResult.getEmail());
        assertNull(actualUpdateAccountResult.getDeletedAt());
        verify(accountRepository).save((Account) any());
        verify(accountRepository).findById((UUID) any());
        verify(passwordEncoder).encode((CharSequence) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#updateAccount(UUID, AccountUpdateDto)}
     */
    @Test
    void shouldThrowWhenIdNotFound() {
        when(accountRepository.save((Account) any())).thenReturn(mock(Account.class));
        when(accountRepository.findById((UUID) any())).thenReturn(Optional.empty());

        when(passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        UUID id = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> accountServiceImpl.updateAccount(id,
                new AccountUpdateDto("janedoe", "iloveyou", "jane.doe@example.org")));
        verify(accountRepository).findById((UUID) any());
    }

    /**
     * Method under test: {@link AccountServiceImpl#updateAccount(UUID, AccountUpdateDto)}
     */
    @Test
    void canUpdateAccount1() {
        Account account = mock(Account.class);
        doThrow(new RuntimeException()).when(account).setEmail((String) any());
        doThrow(new RuntimeException()).when(account).setPassword((String) any());
        doThrow(new RuntimeException()).when(account).setUsername((String) any());
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
        when(accountRepository.save((Account) any())).thenReturn(account1);
        when(accountRepository.findById((UUID) any())).thenReturn(ofResult);
        when(passwordEncoder.encode((CharSequence) any())).thenReturn("secret");
        UUID id = UUID.randomUUID();
        AccountDto actualUpdateAccountResult = accountServiceImpl.updateAccount(id, new AccountUpdateDto());
        assertSame(fromResult, actualUpdateAccountResult.getCreatedAt());
        assertEquals("janedoe", actualUpdateAccountResult.getUsername());
        assertSame(fromResult2, actualUpdateAccountResult.getUpdatedAt());
        assertEquals("iloveyou", actualUpdateAccountResult.getPassword());
        assertEquals(LoginTypeEnum.NORMAL, actualUpdateAccountResult.getLoginType());
        assertSame(randomUUIDResult, actualUpdateAccountResult.getId());
        assertEquals("jane.doe@example.org", actualUpdateAccountResult.getEmail());
        assertSame(fromResult1, actualUpdateAccountResult.getDeletedAt());
        verify(accountRepository).save((Account) any());
        verify(accountRepository).findById((UUID) any());
        verify(account1).getLoginType();
        verify(account1).getEmail();
        verify(account1).getPassword();
        verify(account1).getUsername();
        verify(account1).getCreatedAt();
        verify(account1).getDeletedAt();
        verify(account1).getUpdatedAt();
        verify(account1).getId();
    }

}
