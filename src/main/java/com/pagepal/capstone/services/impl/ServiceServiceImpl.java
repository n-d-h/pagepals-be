package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.dtos.service.WriteServiceDto;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.ServiceMapper;
import com.pagepal.capstone.repositories.postgre.ServiceRepository;
import com.pagepal.capstone.services.ServiceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;

    @Secured({"ADMIN", "STAFF", "READER", "CUSTOMER"})
    @Override
    public ServiceDto serviceById(UUID id) {
        return ServiceMapper.INSTANCE.toDto(serviceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Service not found")));
    }

    @Secured("READER")
    @Override
    public ServiceDto createService(WriteServiceDto writeServiceDto) {
        var service = ServiceMapper.INSTANCE.writeService(writeServiceDto);
        service.setCreatedAt(new Date());
        service.setStatus(Status.ACTIVE);
        service.setTotalOfReview(0);
        service.setTotalOfBooking(0);
        service.setRating(0);
        return ServiceMapper.INSTANCE.toDto(serviceRepository.save(service));
    }

    @Secured("READER")
    @Override
    public ServiceDto updateService(UUID id, WriteServiceDto writeServiceDto) {
        var service = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));
        service.setId(id);
        service.setPrice(writeServiceDto.getPrice());
        service.setDescription(writeServiceDto.getDescription());
        service.setDuration(writeServiceDto.getDuration());
        return ServiceMapper.INSTANCE.toDto(serviceRepository.save(service));
    }

    @Secured("READER")
    @Override
    public String deleteService(UUID id) {
        var service = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));
        service.setStatus(Status.INACTIVE);
        serviceRepository.save(service);
        return "Service deleted";
    }
}
