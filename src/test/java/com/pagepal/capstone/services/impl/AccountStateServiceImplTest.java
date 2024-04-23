package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.repositories.AccountStateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AccountStateServiceImpl.class})
@ExtendWith(SpringExtension.class)
class AccountStateServiceImplTest {
    @MockBean
    private AccountStateRepository accountStateRepository;

    @Autowired
    private AccountStateServiceImpl accountStateServiceImpl;

    /**
     * Method under test: {@link AccountStateServiceImpl#getAccountStates()}
     */
    @Test
    void canGetAccountStates() {
        when(accountStateRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(accountStateServiceImpl.getAccountStates().isEmpty());
        verify(accountStateRepository).findAll();
    }
}

