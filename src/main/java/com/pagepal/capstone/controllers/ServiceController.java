package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.service.QueryDto;
import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.services.ServiceProvideService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceProvideService serviceProvideService;

    @QueryMapping(name = "getServicesByReader")
    public List<ServiceDto> getServicesByReaderId(
            @Argument(name = "readerId") UUID readerId,
            @Argument(name = "filter") QueryDto queryDto
    ) {
        return serviceProvideService.getAllServicesByReaderId(readerId, queryDto);
    }

    @QueryMapping(name = "getServicesByBook")
    public List<ServiceDto> getServicesByBookId(
            @Argument(name = "bookId") UUID bookId,
            @Argument(name = "filter") QueryDto queryDto
    ) {
        return serviceProvideService.getAllServicesByBookId(bookId, queryDto);
    }
}
