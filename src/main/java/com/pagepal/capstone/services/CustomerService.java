package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.customer.CustomerDto;
import com.pagepal.capstone.dtos.customer.CustomerUpdateDto;
import com.pagepal.capstone.dtos.reader.ReaderDto;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDto> getCustomersActive();

    CustomerDto getCustomerById(UUID id);

    CustomerDto updateCustomer(UUID id, CustomerUpdateDto customerUpdateDto);
}
