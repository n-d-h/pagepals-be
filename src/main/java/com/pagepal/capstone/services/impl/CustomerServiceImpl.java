package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.customer.CustomerDto;
import com.pagepal.capstone.dtos.customer.CustomerUpdateDto;
import com.pagepal.capstone.dtos.reader.ReaderDto;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.CustomerMapper;
import com.pagepal.capstone.mappers.ReaderMapper;
import com.pagepal.capstone.repositories.postgre.*;
import com.pagepal.capstone.services.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final AccountRepository accountRepository;
    private final AccountStateRepository accountStateRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;

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

    @Override
    public CustomerDto getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        return CustomerMapper.INSTANCE.toDto(customer);
    }

    @Override
    public CustomerDto updateCustomer(UUID id, CustomerUpdateDto customerUpdateDto) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }

        Customer customer = customerOptional.get();

        if(customerUpdateDto.getFullName() != null) {
            customer.setFullName(customerUpdateDto.getFullName());
        }

        if(customerUpdateDto.getGender() != null) {
            customer.setGender(customerUpdateDto.getGender());
        }

        if(customerUpdateDto.getDob() != null) {
            LocalDate localDate = LocalDate.parse(customerUpdateDto.getDob());
            Date dob = Date.from(localDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant());
            customer.setDob(dob);
        }

        if(customerUpdateDto.getImageUrl() != null) {
            customer.setImageUrl(customerUpdateDto.getImageUrl());
        }

        customer.setUpdatedAt(new Date());

        customerRepository.save(customer);

        return CustomerMapper.INSTANCE.toDto(customer);
    }
}
