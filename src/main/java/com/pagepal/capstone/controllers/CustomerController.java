package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.customer.CustomerDto;
import com.pagepal.capstone.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @QueryMapping
    public List<CustomerDto> getCustomersActive() {
        return customerService.getCustomersActive();
    }
}
