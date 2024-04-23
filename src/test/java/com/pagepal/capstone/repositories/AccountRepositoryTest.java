package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.entities.postgre.AccountState;
import com.pagepal.capstone.entities.postgre.Role;
import com.pagepal.capstone.enums.LoginTypeEnum;
import com.pagepal.capstone.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = {AccountRepository.class, AccountStateRepository.class, RoleRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.pagepal.capstone.entities.postgre"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountStateRepository accountStateRepository;
    @Autowired
    private RoleRepository roleRepository;

    AccountState accountState1 = new AccountState(UUID.randomUUID(), "ACTIVE", Status.ACTIVE, null);
    AccountState accountState2 = new AccountState(UUID.randomUUID(), "INACTIVE", Status.ACTIVE, null);

    //Role
    Role role1 = new Role(UUID.randomUUID(), "READER", Status.ACTIVE, null);
    Role role2 = new Role(UUID.randomUUID(), "CUSTOMER", Status.ACTIVE, null);

    //Account
    Account account1 = new Account(UUID.randomUUID(), "username1", "password1", "email1", "fullName1","0123456789",  LoginTypeEnum.NORMAL,
            new Date(), new Date(),new Date(), accountState1,null,null, null, null, role1, null, null);
    Account account2 = new Account(UUID.randomUUID(), "username2", "password2", "email2","fullName1","0123456789", LoginTypeEnum.NORMAL,
            new Date(), new Date(),new Date(), accountState2,null,null, null, null, role2, null, null);


    /**
     * Method under test: {@link AccountRepository#findByUsername(String)}
     */
    @Test
    void canFindByUsername() {
        // Arrange
        roleRepository.save(role1);
        roleRepository.save(role2);
        accountStateRepository.save(accountState1);
        accountStateRepository.save(accountState2);
        accountRepository.save(account1);
        accountRepository.save(account2);

        // Act
        Optional<Account> actualFindByUsernameResult = this.accountRepository.findByUsername("username1");

        // Assert
        assertEquals("username1", actualFindByUsernameResult.get().getUsername());
        assertEquals("password1", actualFindByUsernameResult.get().getPassword());
    }

    /**
     * Method under test: {@link AccountRepository#findByUsername(String)}
     */
    @Test
    void shouldReturnEmptyWhenFindAccountByUserName() {
        // Arrange
        roleRepository.save(role1);
        roleRepository.save(role2);
        accountStateRepository.save(accountState1);
        accountStateRepository.save(accountState2);
        accountRepository.save(account1);
        accountRepository.save(account2);

        // Act
        Optional<Account> actualFindByUsernameResult = this.accountRepository.findByUsername("username3");

        // Assert
        assertTrue(actualFindByUsernameResult.isEmpty());
    }

    /**
     * Method under test: {@link AccountRepository#findByAccountStateAndRole(AccountState, Role)}
     */
    @Test
    void canFindByAccountStateAndRole() {
        // Arrange
        roleRepository.save(role1);
        roleRepository.save(role2);
        accountStateRepository.save(accountState1);
        accountStateRepository.save(accountState2);
        accountRepository.save(account1);
        accountRepository.save(account2);

        // Act
        List<Account> actualFindByAccountStateAndRoleResult = this.accountRepository.findByAccountStateAndRole(accountState1,
                role1);

        // Assert
        assertEquals(1, actualFindByAccountStateAndRoleResult.size());
        assertEquals("username1", actualFindByAccountStateAndRoleResult.get(0).getUsername());
    }

    /**
     * Method under test: {@link AccountRepository#findByAccountStateAndRole(AccountState, Role)}
     */
    @Test
    void shouldReturnEmptyListWhenNotFound() {
        // Arrange
        roleRepository.save(role1);
        roleRepository.save(role2);
        accountStateRepository.save(accountState1);
        accountStateRepository.save(accountState2);
        accountRepository.save(account1);
        accountRepository.save(account2);

        // Act
        List<Account> actualFindByAccountStateAndRoleResult = this.accountRepository.findByAccountStateAndRole(accountState1,
                role2);

        // Assert
        assertTrue(actualFindByAccountStateAndRoleResult.isEmpty());
    }
}

