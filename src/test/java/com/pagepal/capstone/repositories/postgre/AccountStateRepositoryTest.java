package com.pagepal.capstone.repositories.postgre;

import com.pagepal.capstone.entities.postgre.AccountState;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.AccountStateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = {AccountStateRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.pagepal.capstone.entities.postgre"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class AccountStateRepositoryTest {
    @Autowired
    private AccountStateRepository accountStateRepository;

    /**
     * Method under test: {@link AccountStateRepository#findByNameAndStatus(String, Status)}
     */
    @Test
    void shouldReturnActualAccountState() {
        // Arrange
        AccountState accountState1 = new AccountState(UUID.randomUUID(), "ACTIVE", Status.ACTIVE, null);
        AccountState accountState2 = new AccountState(UUID.randomUUID(), "INACTIVE", Status.ACTIVE, null);
        this.accountStateRepository.save(accountState1);
        // Act
        Optional<AccountState> actualFindByNameAndStatusResult = this.accountStateRepository.findByNameAndStatus("ACTIVE",
                Status.ACTIVE);

        // Assert
        assertEquals("ACTIVE", actualFindByNameAndStatusResult.get().getName());
    }

    @Test
    void shouldReturnNullWhenFindWrongName() {
        // Arrange
        AccountState accountState1 = new AccountState(UUID.randomUUID(), "ACTIVE", Status.ACTIVE, null);
        AccountState accountState2 = new AccountState(UUID.randomUUID(), "INACTIVE", Status.ACTIVE, null);
        this.accountStateRepository.save(accountState1);
        // Act
        Optional<AccountState> actualFindByNameAndStatusResult = this.accountStateRepository.findByNameAndStatus("BLOCK",
                Status.ACTIVE);

        // Assert
        assertTrue(actualFindByNameAndStatusResult.isEmpty());
    }

}

