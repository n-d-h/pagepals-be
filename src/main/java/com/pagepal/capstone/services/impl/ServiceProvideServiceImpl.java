package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.mappers.ServiceMapper;
import com.pagepal.capstone.repositories.postgre.ReaderRepository;
import com.pagepal.capstone.repositories.postgre.ServiceRepository;
import com.pagepal.capstone.services.ServiceProvideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceProvideServiceImpl implements ServiceProvideService {

    private final ServiceRepository serviceRepository;
    private final ReaderRepository readerRepository;

    @Override
    public List<ServiceDto> getAllServicesByReaderId(UUID readerId) {
        var reader = readerRepository.findById(readerId).orElseThrow(
                () -> new RuntimeException("Reader not found")
        );
        var services = serviceRepository.findAllByReader(reader);
        return services.stream().map(ServiceMapper.INSTANCE::toDto).collect(Collectors.toList());
    }
}
