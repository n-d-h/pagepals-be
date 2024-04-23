package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.dtos.service.ServiceUpdate;
import com.pagepal.capstone.dtos.service.WriteServiceDto;
import com.pagepal.capstone.dtos.servicetype.ServiceTypeDto;
import com.pagepal.capstone.entities.postgre.Book;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.ServiceType;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.ServiceMapper;
import com.pagepal.capstone.repositories.*;
import com.pagepal.capstone.services.BookService;
import com.pagepal.capstone.services.ServiceService;
import com.pagepal.capstone.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {
    private final ServiceRepository serviceRepository;
    private final ServiceTypeRepository serviceTypeRepository;

    private final BookRepository bookRepository;

    private final ReaderRepository readerRepository;

    private final CategoryRepository categoryRepository;

    private final AuthorRepository authorRepository;
    private final BookService bookService;
    private final DateUtils dateUtils;

    @Secured({"ADMIN", "STAFF", "READER", "CUSTOMER"})
    @Override
    public ServiceDto serviceById(UUID id) {
        return ServiceMapper.INSTANCE.toDto(serviceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Service not found")));
    }


    @Secured("READER")
    @Override
    public ServiceDto updateService(UUID id, ServiceUpdate writeServiceDto) {
        var service = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));
        service.setId(id);
        service.setPrice(writeServiceDto.getPrice());
        service.setDescription(writeServiceDto.getDescription());
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

    @Override
    public List<ServiceTypeDto> getListServiceType() {
        return serviceTypeRepository.findAll().stream().map(ServiceMapper.INSTANCE::toServiceTypeDto).toList();
    }

    @Override
    public ServiceDto createService(WriteServiceDto writeServiceDto) {
        ServiceType serviceType = serviceTypeRepository.findById(writeServiceDto.getServiceTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Service type not found"));

        Reader reader = readerRepository.findById(writeServiceDto.getReaderId())
                .orElseThrow(() -> new EntityNotFoundException("Reader not found"));

        Book book = bookRepository.findByExternalId(writeServiceDto.getBook().getId()).orElse(null);

        if (book == null) {
            book = bookService.createNewBook(writeServiceDto.getBook());
        }
        var service = new com.pagepal.capstone.entities.postgre.Service();
        service.setPrice(writeServiceDto.getPrice());
        service.setDescription(writeServiceDto.getDescription());
        service.setDuration(writeServiceDto.getDuration());
        service.setServiceType(serviceType);
        service.setReader(reader);
        service.setBook(book);
        service.setCreatedAt(dateUtils.getCurrentVietnamDate());
        service.setRating(0);
        service.setTotalOfBooking(0);
        service.setTotalOfReview(0);
        service.setStatus(Status.ACTIVE);
        return ServiceMapper.INSTANCE.toDto(serviceRepository.save(service));
    }

    @Override
    public List<ServiceDto> getListServiceByServiceTypeAndBook(UUID serviceTypeId, UUID bookId) {
        ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Service type not found"));
        Book book = bookRepository.findById(bookId).orElseThrow();
        return serviceRepository.findByServiceTypeAndBook(serviceType, book)
                .stream().map(ServiceMapper.INSTANCE::toDto).toList();
    }

}
