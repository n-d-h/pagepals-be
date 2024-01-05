package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.mappers.ServiceMapper;
import com.pagepal.capstone.repositories.postgre.ServiceRepository;
import com.pagepal.capstone.services.ServiceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;

    @Override
    public ServiceDto serviceById(UUID id) {
        return ServiceMapper.INSTANCE.toDto(serviceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Service not found")));
    }
}
