package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.service.QueryDto;
import com.pagepal.capstone.dtos.service.ServiceCustomerDto;
import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.dtos.service.WriteServiceDto;
import com.pagepal.capstone.dtos.servicetype.ServiceTypeDto;
import com.pagepal.capstone.services.ServiceProvideService;
import com.pagepal.capstone.services.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceProvideService serviceProvideService;
    private final ServiceService serviceService;

    @QueryMapping(name = "getServicesByReader")
    public List<ServiceCustomerDto> getServicesByReaderId(
            @Argument(name = "readerId") UUID readerId,
            @Argument(name = "filter") QueryDto queryDto
    ) {
        return serviceProvideService.getAllServicesByReaderId(readerId, queryDto);
    }

    @QueryMapping(name = "getServicesByBook")
    public List<ServiceCustomerDto> getServicesByBookId(
            @Argument(name = "bookId") UUID bookId,
            @Argument(name = "filter") QueryDto queryDto
    ) {
        return serviceProvideService.getAllServicesByBookId(bookId, queryDto);
    }

    @QueryMapping(name = "serviceById")
    public ServiceDto serviceById(@Argument(name = "id") UUID id) {
        return serviceService.serviceById(id);
    }

    @MutationMapping(name = "updateService")
    public ServiceDto updateService(
            @Argument(name = "id") UUID id,
            @Argument(name = "service") WriteServiceDto writeServiceDto) {
        return serviceService.updateService(id, writeServiceDto);
    }

    @MutationMapping(name = "deleteService")
    public String deleteService(@Argument(name = "id") UUID id) {
        return serviceService.deleteService(id);
    }

    @QueryMapping
    public List<ServiceTypeDto> getListServiceType() {
        return serviceService.getListServiceType();
    }

    @MutationMapping(name = "createService")
    public ServiceDto createService(@Argument(name = "service") WriteServiceDto writeServiceDto) {
        return serviceService.createService(writeServiceDto);
    }
}
