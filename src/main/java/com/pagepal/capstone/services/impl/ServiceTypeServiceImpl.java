package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.servicetype.ServiceTypeDto;
import com.pagepal.capstone.mappers.ServiceMapper;
import com.pagepal.capstone.repositories.BookRepository;
import com.pagepal.capstone.repositories.ReaderRepository;
import com.pagepal.capstone.repositories.ServiceRepository;
import com.pagepal.capstone.repositories.ServiceTypeRepository;
import com.pagepal.capstone.services.ServiceTypeService;
import jakarta.persistence.EntityNotFoundException;
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
    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;

    @Override
    public List<ServiceTypeDto> getListServiceTypesByService(UUID readerId, UUID bookId) {
        var reader = readerRepository.findById(readerId).orElseThrow(() -> new EntityNotFoundException("Reader not found"));
        var book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        var services = serviceRepository.findByReaderAndBook(reader, book);
        var serviceTypes = serviceTypeRepository.findAllByServices(services);
        return serviceTypes.stream().map(ServiceMapper.INSTANCE::toServiceTypeDto).toList();
    }
}
