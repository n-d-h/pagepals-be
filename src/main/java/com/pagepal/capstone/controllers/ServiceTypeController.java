package com.pagepal.capstone.controllers;

import com.pagepal.capstone.dtos.servicetype.ServiceTypeDto;
import com.pagepal.capstone.services.ServiceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ServiceTypeController {
    private final ServiceTypeService serviceTypeService;

    @QueryMapping(name = "getListServiceTypesByService")
    public List<ServiceTypeDto> getListServiceTypesByService(@Argument(name = "services") List<UUID> serviceIds) {
        return serviceTypeService.getListServiceTypesByService(serviceIds);
    }
}
