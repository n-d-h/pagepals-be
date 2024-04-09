package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.customer.CustomerDto;
import com.pagepal.capstone.dtos.customer.CustomerReadDto;
import com.pagepal.capstone.dtos.customer.CustomerUpdateDto;
import com.pagepal.capstone.entities.postgre.Account;
import com.pagepal.capstone.entities.postgre.AccountState;
import com.pagepal.capstone.entities.postgre.Customer;
import com.pagepal.capstone.entities.postgre.Role;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.CustomerMapper;
import com.pagepal.capstone.repositories.AccountRepository;
import com.pagepal.capstone.repositories.AccountStateRepository;
import com.pagepal.capstone.repositories.CustomerRepository;
import com.pagepal.capstone.repositories.RoleRepository;
import com.pagepal.capstone.services.CustomerService;
import com.pagepal.capstone.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
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
    private final DateUtils dateUtils;

    @Secured({"STAFF", "ADMIN"})
    @Override
    public List<CustomerDto> getCustomersActive() {
        AccountState accountState = accountStateRepository
                .findByNameAndStatus("ACTIVE", Status.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Account State not found"));
        Role role = roleRepository
                .findByName("CUSTOMER")
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        List<Account> accounts = accountRepository.findByAccountStateAndRole(accountState, role);
        List<Customer> customers = accounts.stream().map(Account::getCustomer).toList();
        return customers.stream().map(CustomerMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Secured({"CUSTOMER", "READER"})
    @Override
    public CustomerDto getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        return CustomerMapper.INSTANCE.toDto(customer);
    }

    @Secured({"CUSTOMER", "READER"})
    @Override
    public CustomerReadDto getCustomerProfile(UUID id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        if (customer.getAccount().getAccountState().getName().equals("ACTIVE")) {
            return CustomerMapper.INSTANCE.toDtoRead(customer);
        } else throw new EntityNotFoundException("Customer not found");
    }

    @Secured({"CUSTOMER", "READER"})
    @Override
    public CustomerDto updateCustomer(UUID id, CustomerUpdateDto customerUpdateDto) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isEmpty()) {
            throw new EntityNotFoundException("Customer not found");
        }

        Customer customer = customerOptional.get();

        if (customerUpdateDto.getFullName() != null) {
            customer.setFullName(customerUpdateDto.getFullName());
        }

        if (customerUpdateDto.getGender() != null) {
            customer.setGender(customerUpdateDto.getGender());
        }

        if (customerUpdateDto.getDob() != null) {
            LocalDate localDate = LocalDate.parse(customerUpdateDto.getDob());
            Date dob = Date.from(localDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant());
            customer.setDob(dob);
        }

        if (customerUpdateDto.getImageUrl() != null) {
            customer.setImageUrl(customerUpdateDto.getImageUrl());
        }

        customer.setUpdatedAt(dateUtils.getCurrentVietnamDate());

        customerRepository.save(customer);

        return CustomerMapper.INSTANCE.toDto(customer);
    }
}
