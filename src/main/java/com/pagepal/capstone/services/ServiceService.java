package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.dtos.service.WriteServiceDto;

import java.util.UUID;

public interface ServiceService {
    ServiceDto serviceById(UUID id);

    ServiceDto updateService(UUID id, WriteServiceDto writeServiceDto);

    String deleteService(UUID id);
}
