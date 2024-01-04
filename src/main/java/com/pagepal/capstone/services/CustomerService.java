package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.customer.CustomerDto;
import com.pagepal.capstone.dtos.reader.ReaderDto;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getCustomersActive();
}
