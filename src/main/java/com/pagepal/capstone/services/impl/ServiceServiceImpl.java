package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.service.*;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.ServiceMapper;
import com.pagepal.capstone.repositories.postgre.ServiceRepository;
import com.pagepal.capstone.services.ServiceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Override
    public ServiceDto createService(WriteServiceDto writeServiceDto) {
        com.pagepal.capstone.entities.postgre.Service service = ServiceMapper.INSTANCE.writeService(writeServiceDto);
        service.setCreatedAt(new Date());
        service.setStatus(Status.ACTIVE);
        service.setTotalOfReview(0);
        service.setTotalOfBooking(0);
        service.setRating(0);
        return ServiceMapper.INSTANCE.toDto(serviceRepository.save(service));
    }

    @Override
    public ServiceDto updateService(UUID id, WriteServiceDto writeServiceDto) {
        com.pagepal.capstone.entities.postgre.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));
        service.setId(id);
        service.setPrice(writeServiceDto.getPrice());
        service.setDescription(writeServiceDto.getDescription());
        service.setDuration(writeServiceDto.getDuration());
        return ServiceMapper.INSTANCE.toDto(serviceRepository.save(service));
    }

    @Override
    public String deleteService(UUID id) {
        com.pagepal.capstone.entities.postgre.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));
        service.setStatus(Status.INACTIVE);
        serviceRepository.save(service);
        return "Service deleted";
    }
}
