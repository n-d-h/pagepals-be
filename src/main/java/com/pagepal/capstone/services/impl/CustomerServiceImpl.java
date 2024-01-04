package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.customer.CustomerDto;
import com.pagepal.capstone.dtos.reader.ReaderDto;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.CustomerMapper;
import com.pagepal.capstone.mappers.ReaderMapper;
import com.pagepal.capstone.repositories.postgre.AccountRepository;
import com.pagepal.capstone.repositories.postgre.AccountStateRepository;
import com.pagepal.capstone.repositories.postgre.ReaderRepository;
import com.pagepal.capstone.repositories.postgre.RoleRepository;
import com.pagepal.capstone.services.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final ReaderRepository readerRepository;
    private final AccountRepository accountRepository;
    private final AccountStateRepository accountStateRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<CustomerDto> getCustomersActive() {
        AccountState accountState = accountStateRepository
                .findByNameAndStatus("ACTIVE", Status.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Account State not found"));
        Role role = roleRepository
                .findByName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        List<Account> accounts = accountRepository.findByAccountStateAndRole(accountState, role);
        List<Customer> customers = accounts.stream().map(Account::getCustomer).toList();
        return customers.stream().map(CustomerMapper.INSTANCE::toDto).collect(Collectors.toList());
    }
}
