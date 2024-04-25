package com.pagepal.capstone.services;

import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.dtos.service.ServiceUpdate;
import com.pagepal.capstone.dtos.service.WriteServiceDto;
import com.pagepal.capstone.dtos.servicetype.ServiceTypeDto;

import java.util.List;
import java.util.UUID;

public interface ServiceService {
    ServiceDto serviceById(UUID id);

    ServiceDto updateService(UUID id, ServiceUpdate writeServiceDto);

    ServiceDto keepBookingAndUpdateService(UUID id, ServiceUpdate writeServiceDto);

    String deleteService(UUID id);

    String keepBookingAndDeleteService(UUID id);

    List<ServiceTypeDto> getListServiceType();

    ServiceDto createService(WriteServiceDto writeServiceDto);

    List<ServiceDto> getListServiceByServiceTypeAndBook(UUID serviceTypeId, UUID bookId);
}
