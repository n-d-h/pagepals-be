package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.servicetype.ServiceTypeDto;
import com.pagepal.capstone.mappers.ServiceMapper;
import com.pagepal.capstone.repositories.ServiceRepository;
import com.pagepal.capstone.repositories.ServiceTypeRepository;
import com.pagepal.capstone.services.ServiceTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class ServiceTypeServiceImpl implements ServiceTypeService {
    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceRepository serviceRepository;

    @Override
    public List<ServiceTypeDto> getListServiceTypesByService(List<UUID> serviceIds) {
        var services = serviceIds.stream().map(uuid -> serviceRepository.findById(uuid).orElse(null)).toList();
        var serviceTypes = serviceTypeRepository.findAllByServices(services);
        return serviceTypes.stream().map(ServiceMapper.INSTANCE::toServiceTypeDto).toList();
    }
}
