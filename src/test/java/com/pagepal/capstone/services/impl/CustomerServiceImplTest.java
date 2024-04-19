package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.customer.CustomerDto;
import com.pagepal.capstone.dtos.customer.CustomerReadDto;
import com.pagepal.capstone.dtos.customer.CustomerUpdateDto;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.GenderEnum;
import com.pagepal.capstone.enums.LoginTypeEnum;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.repositories.AccountRepository;
import com.pagepal.capstone.repositories.AccountStateRepository;
import com.pagepal.capstone.repositories.CustomerRepository;
import com.pagepal.capstone.repositories.RoleRepository;
import com.pagepal.capstone.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CustomerServiceImpl.class})
@ExtendWith(SpringExtension.class)
class CustomerServiceImplTest {
    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private AccountStateRepository accountStateRepository;

    @Autowired
    private CustomerServiceImpl customerServiceImpl;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private DateUtils dateUtils;

    //Mock data
    //Account State
    AccountState accountState1 = new AccountState(UUID.randomUUID(), "ACTIVE", Status.ACTIVE, null);
    AccountState accountState2 = new AccountState(UUID.randomUUID(), "INACTIVE", Status.ACTIVE, null);

    //Role
    Role role1 = new Role(UUID.randomUUID(), "READER", Status.ACTIVE, null);
    Role role2 = new Role(UUID.randomUUID(), "CUSTOMER", Status.ACTIVE, null);

    //Account
    Account account1 = new Account(UUID.randomUUID(), "username1", "password1", "email1","fullName1","0123456789", LoginTypeEnum.NORMAL,
            new Date(), new Date(),new Date(), accountState1,null,null, null, null, role1, null, null);
    Account account2 = new Account(UUID.randomUUID(), "username2", "password2", "email2","fullName1","0123456789", LoginTypeEnum.NORMAL,
            new Date(), new Date(),new Date(), accountState2,null,null, null, null, role2, null, null);
    Account account3 = new Account(UUID.randomUUID(), "username3", "password3", "email3","fullName1","0123456789", LoginTypeEnum.NORMAL,
            new Date(), new Date(),new Date(), accountState1,null,null, null, null, role2, null, null);
    //Reader
    Reader reader1 = new Reader(UUID.randomUUID(), "name1", 5, "genre1", "Vietnamese", "accent1" ,
            "url" ,"des1", 0, 0, "url","avt",  123.2, "tag",
            new Date(), new Date(), new Date(), null,null, null, account1, null, null, null, null,
            null, null, null, null);
    Customer customer1 = new Customer(UUID.fromString("6ff8f184-e668-4d51-ab18-89ec7d2ba014"),"customer name 1", GenderEnum.MALE, new Date(), "url",
            new Date(), new Date(), new Date(), Status.ACTIVE, account2, null, null, new ArrayList<>(), null);

    Customer customer2 = new Customer(UUID.fromString("6ff8f184-e668-4d51-ab18-89ec7d2ba014"),"customer name 1", GenderEnum.MALE, new Date(), "url",
            new Date(), new Date(), new Date(), Status.ACTIVE, account1, null, null, new ArrayList<>(), null);

    /**
     * Method under test: {@link ReaderServiceImpl#getReadersActive()}
     */
    @Test
    void canGetCustomersActive() {
        account1.setReader(reader1);
        account2.setCustomer(customer1);
        when(accountRepository.findByAccountStateAndRole(accountState1, role2)).thenReturn(Collections.singletonList(account2));
        when(accountStateRepository.findByNameAndStatus("ACTIVE", Status.ACTIVE))
                .thenReturn(Optional.of(accountState1));
        when(roleRepository.findByName("CUSTOMER")).thenReturn(Optional.of(role2));
        CustomerDto customerDto = customerServiceImpl.getCustomersActive().get(0);
        assertEquals(customerDto.getFullName(), "customer name 1");
        verify(accountRepository).findByAccountStateAndRole(accountState1, role2);
        verify(accountStateRepository).findByNameAndStatus("ACTIVE", Status.ACTIVE);
        verify(roleRepository).findByName("CUSTOMER");
    }

    /**
     * Method under test: {@link ReaderServiceImpl#getReadersActive()}
     */
    @Test
    void shouldReturnEmptyCustomer() {
        account1.setReader(reader1);
        account2.setCustomer(customer1);
        when(accountRepository.findByAccountStateAndRole(accountState1, role2)).thenReturn(new ArrayList<>());
        when(accountStateRepository.findByNameAndStatus("ACTIVE", Status.ACTIVE))
                .thenReturn(Optional.of(accountState1));
        when(roleRepository.findByName("CUSTOMER")).thenReturn(Optional.of(role2));
        assertTrue(customerServiceImpl.getCustomersActive().isEmpty());
        verify(accountRepository).findByAccountStateAndRole(accountState1, role2);
        verify(accountStateRepository).findByNameAndStatus("ACTIVE", Status.ACTIVE);
        verify(roleRepository).findByName("CUSTOMER");
    }

    /**
     * Method under test: {@link ReaderServiceImpl#getReadersActive()}
     */
    @Test
    void shouldThrowRunTimeExceptionWhenGetReader1() {
        account1.setReader(reader1);
        account2.setCustomer(customer1);

        when(accountRepository.findByAccountStateAndRole(accountState1, role2))
                .thenReturn(Collections.singletonList(account2));
        when(accountStateRepository.findByNameAndStatus("ACTIVE", Status.ACTIVE))
                .thenReturn(Optional.of(accountState1));
        when(roleRepository.findByName("CUSTOMER")).thenThrow(new EntityNotFoundException());

        assertThrows(EntityNotFoundException.class, () -> {
            customerServiceImpl.getCustomersActive();
        });
    }

    /**
     * Method under test: {@link ReaderServiceImpl#getReadersActive()}
     */
    @Test
    void shouldThrowRunTimeExceptionWhenGetReader2() {
        account1.setReader(reader1);
        account2.setCustomer(customer1);

        when(accountRepository.findByAccountStateAndRole(accountState1, role1))
                .thenReturn(Collections.singletonList(account1));
        when(accountStateRepository.findByNameAndStatus("ACTIVE", Status.ACTIVE))
                .thenThrow(new EntityNotFoundException());
        when(roleRepository.findByName("CUSTOMER")).thenReturn(Optional.of(role2));

        assertThrows(EntityNotFoundException.class, () -> {
            customerServiceImpl.getCustomersActive();
        });
    }

    /**
     * Method under test: {@link CustomerServiceImpl#getCustomerById(UUID)}
     */
    @Test
    void canGetCustomerDetail() {
        account1.setReader(reader1);
        account2.setCustomer(customer1);
        when(customerRepository.findById(UUID.fromString("6ff8f184-e668-4d51-ab18-89ec7d2ba014")))
                .thenReturn(Optional.of(customer1));
        CustomerDto customer = customerServiceImpl.getCustomerById(UUID.fromString("6ff8f184-e668-4d51-ab18-89ec7d2ba014"));
        assertEquals(customer.getFullName(), "customer name 1");
        verify(customerRepository).findById(UUID.fromString("6ff8f184-e668-4d51-ab18-89ec7d2ba014"));
    }

    @Test
    void testUpdateCustomer() throws ParseException {
        account1.setReader(reader1);
        account2.setCustomer(customer1);
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer1));
        when(customerRepository.save(any())).thenReturn(customer1);

        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto();
        customerUpdateDto.setFullName("customer name 1");
        customerUpdateDto.setGender(GenderEnum.MALE);
        customerUpdateDto.setDob("2021-08-01");
        customerUpdateDto.setImageUrl("url");

        CustomerDto customerDto = customerServiceImpl.updateCustomer(
                UUID.fromString("6ff8f184-e668-4d51-ab18-89ec7d2ba014"), customerUpdateDto);

        assertEquals(customerDto.getFullName(), "customer name 1");
    }

    /**
     * Method under test: {@link CustomerServiceImpl#getCustomerProfile(UUID)}
     */
    @Test
    void canGetCustomerProfile() {
        account3.setCustomer(customer2);
        when(customerRepository.findById(UUID.fromString("6ff8f184-e668-4d51-ab18-89ec7d2ba014")))
                .thenReturn(Optional.of(customer2));
        CustomerReadDto customer = customerServiceImpl.getCustomerProfile(UUID.fromString("6ff8f184-e668-4d51-ab18-89ec7d2ba014"));
        assertEquals(customer.getFullName(), customer2.getFullName());
        verify(customerRepository).findById(UUID.fromString("6ff8f184-e668-4d51-ab18-89ec7d2ba014"));
    }
}

