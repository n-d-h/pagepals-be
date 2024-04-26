package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.entities.postgre.AccountState;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.Role;
import com.pagepal.capstone.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration(classes = {ReaderRepository.class, AccountRepository.class, AccountStateRepository.class, RoleRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.pagepal.capstone.entities.postgre"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class ReaderRepositoryTest {
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountStateRepository accountStateRepository;
    @Autowired
    private RoleRepository roleRepository;

    //Date
    LocalDate localDate1 = LocalDate.of(2023, 12, 21);
    LocalDate localDate2 = LocalDate.of(2022, 12, 21);
    LocalDate localDate3 = LocalDate.of(2021, 12, 21);
    Date date1 = java.sql.Date.valueOf(localDate1);
    Date date2 = java.sql.Date.valueOf(localDate2);
    Date date3 = java.sql.Date.valueOf(localDate3);

    //Reader
    Reader reader1 = new Reader(UUID.randomUUID(), "name", 5, "genre", ""
            , "", "link1", "des1", null,
            null, "vid1","avt",
            date2, date2, date2, Status.ACTIVE, null, null,null, null,
            null, null, null, null,null);

    Reader reader2 = new Reader(UUID.randomUUID(), "name", 5, "genre", ""
            , "accent2", "link2", "des2", null,
            null, "vid2","avt",
            date2, date2, date2, Status.ACTIVE, null, null,null, null,
            null, null, null, null,null);

    Reader reader3 = new Reader(UUID.randomUUID(), "name3", 4, "genre3", "Vietnamese"
            , "accent3", "link3", "des3", null,
            null, "vid3","avt",
            date2, date2, date2, Status.ACTIVE, null, null,null, null,
            null, null, null, null,null);

    private List<Reader> result;



    @Test
    void shouldReturnEmptyListWhenNoReaderFoundWithFindingByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCaseAndRating() {
        // Arrange
        readerRepository.saveAll(Arrays.asList(reader1, reader2, reader3));
        // Act
        Page<Reader> result = readerRepository.findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCaseAndRatingAndAccountState("nothing", "genre", "language", "accent", 4, "READER_ACTIVE",PageRequest.of(0, 10));
        // Assert
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        assertNotNull(result);
    }

    @Test
    void shouldReturnEmptyListWhenNoReaderFoundWithFindingByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCase() {
        // Arrange
        readerRepository.saveAll(Arrays.asList(reader1, reader2, reader3));
        // Act
        Page<Reader> result = readerRepository.findByNicknameContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCaseAndCountryAccentContainingIgnoreCaseAndAccountState("nothing", "genre", "language", "accent", "READER_ACTIVE",PageRequest.of(0, 10));
        // Assert
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        assertNotNull(result);
    }

    /**
     * Method under test: {@link ReaderRepository#findTop8ByAccountInOrderByRatingDesc(List)}
     */

    @Test
    void canFindTop8ByAccountInOrderByRatingDesc() {
        // Arrange

        /* Role */
        Role role1 = new Role();
        role1.setName("READER");
        role1.setStatus(Status.ACTIVE);


        roleRepository.save(role1);

        /* AccountState */
        AccountState accountState = new AccountState();
        accountState.setName("ACTIVE");
        accountState.setStatus(Status.ACTIVE);

        accountStateRepository.save(accountState);

        /* Account */

        Account account1 = new Account();
        account1.setRole(role1);
        account1.setAccountState(accountState);

        Account account2 = new Account();
        account2.setRole(role1);
        account2.setAccountState(accountState);

        Account account3 = new Account();
        account3.setRole(role1);
        account3.setAccountState(accountState);

        accountRepository.saveAll(Arrays.asList(account1, account2, account3));

        /* Reader */

        reader1.setAccount(account1);
        reader2.setAccount(account2);
        reader3.setAccount(account3);

        readerRepository.saveAll(Arrays.asList(reader1, reader2, reader3));
        // Act
        List<Reader> result = readerRepository.findTop8ByAccountInOrderByRatingDesc(Arrays.asList(account1, account2, account3));
        // Assert
        assertEquals(3, result.size());
        assertNotNull(result);
    }
}
