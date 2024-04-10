package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.customer.CustomerDto;
import com.pagepal.capstone.dtos.customer.CustomerReadDto;
import com.pagepal.capstone.dtos.customer.CustomerUpdateDto;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDto> getCustomersActive();

    CustomerDto getCustomerById(UUID id);

    CustomerReadDto getCustomerProfile(UUID id);

    CustomerDto updateCustomer(UUID id, CustomerUpdateDto customerUpdateDto) throws ParseException;
}
