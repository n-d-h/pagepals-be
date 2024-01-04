package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.services.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService serviceService;

    @QueryMapping(name = "service")
    public ServiceDto serviceById(@Argument(name = "id") UUID id) {
        return serviceService.serviceById(id);
    }
}
