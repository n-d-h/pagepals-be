package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.customer.CustomerDto;
import com.pagepal.capstone.dtos.customer.CustomerUpdateDto;
import com.pagepal.capstone.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @QueryMapping
    public List<CustomerDto> getCustomersActive() {
        return customerService.getCustomersActive();
    }

    @QueryMapping
    public CustomerDto getCustomerDetail(@Argument UUID id) {
        return customerService.getCustomerById(id);
    }

    @MutationMapping(name = "updateCustomer")
    public CustomerDto updateCustomer(@Argument UUID id, @Argument(name = "customer") CustomerUpdateDto customerUpdateDto) {
        return customerService.updateCustomer(id, customerUpdateDto);
    }
}
