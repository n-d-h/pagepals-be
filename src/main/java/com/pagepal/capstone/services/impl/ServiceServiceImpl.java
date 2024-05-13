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
import com.pagepal.capstone.services.BookingService;
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
    private final BookingRepository bookingRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final CategoryRepository categoryRepository;

    private final AuthorRepository authorRepository;
    private final BookingService bookingService;
    private final BookService bookService;
    private final DateUtils dateUtils;

    @Override
    public ServiceDto serviceById(UUID id) {
        var service = serviceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Service not found"));
        if (service.getStatus() == Status.INACTIVE || Boolean.TRUE.equals(service.getIsDeleted())) {
            throw new EntityNotFoundException("Service not found");
        }
        return ServiceMapper.INSTANCE.toDto(service);
    }

    private Boolean checkServiceIsInPendingBooking(UUID serviceId) {
        return bookingRepository.countPendingBookingByService(serviceId) > 0;
    }

    private Boolean checkServiceIsInBooking(UUID serviceId) {
        return bookingRepository.countBookingByService(serviceId) > 0;
    }

    private Boolean checkServiceIsInCompletedOrCanceledBooking(UUID serviceId) {
        return bookingRepository.countStateBookingByService(serviceId) > 0;
    }

    @Secured("READER")
    @Override
    public ServiceDto updateService(UUID id, ServiceUpdate writeServiceDto) {
        var service = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));


        // If service is not in booking, we update service
        if (Boolean.FALSE.equals(checkServiceIsInBooking(id))) {
            service.setId(id);
            service.setPrice(writeServiceDto.getPrice());
            service.setDescription(writeServiceDto.getDescription());
            service.setImageUrl(writeServiceDto.getImageUrl());
        }
        // If service is in pending booking, we throw exception
        else if (Boolean.TRUE.equals(checkServiceIsInPendingBooking(id))) {
            throw new IllegalStateException("Service is in pending booking");
        }
        // if service is in completed or canceled booking, we delete old service and create new service
        // with the same information
        else if (Boolean.TRUE.equals(checkServiceIsInCompletedOrCanceledBooking(id))) {
            // create new service
            var clone = createCloneService(writeServiceDto, service);

            // delete old service
            service.setIsDeleted(true);
            serviceRepository.save(service);

            return clone;
        }
        return ServiceMapper.INSTANCE.toDto(serviceRepository.save(service));
    }

    @Override
    public ServiceDto keepBookingAndUpdateService(UUID id, ServiceUpdate writeServiceDto) {
        var existingService = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));

        if (Boolean.TRUE.equals(checkServiceIsInPendingBooking(id))) {
            // Create a new clone of the existing service
            var clone = createCloneService(writeServiceDto, existingService);

            // Delete the existing service
            existingService.setIsDeleted(true);
            serviceRepository.save(existingService);
            return clone;
        } else if (Boolean.TRUE.equals(checkServiceIsInCompletedOrCanceledBooking(id))) {
            throw new IllegalStateException("Service is not in pending booking");
        } else throw new IllegalStateException("Service is not in booking");
    }

    private ServiceDto createCloneService(ServiceUpdate writeServiceDto, com.pagepal.capstone.entities.postgre.Service existingService) {
        var service = new com.pagepal.capstone.entities.postgre.Service();
        service.setPrice(writeServiceDto.getPrice());
        service.setDescription(writeServiceDto.getDescription());
        service.setImageUrl(writeServiceDto.getImageUrl());
        service.setDuration(existingService.getDuration());
        service.setServiceType(existingService.getServiceType());
        service.setReader(existingService.getReader());
        service.setBook(existingService.getBook());
        service.setCreatedAt(existingService.getCreatedAt());
        service.setRating(0);
        service.setTotalOfBooking(0);
        service.setTotalOfReview(0);
        service.setIsDeleted(false);
        service.setStatus(Status.ACTIVE);
        return ServiceMapper.INSTANCE.toDto(serviceRepository.save(service));
    }


    @Secured("READER")
    @Override
    public String deleteService(UUID id) {
        var service = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));
        if (Boolean.TRUE.equals(checkServiceIsInPendingBooking(id))) {
            throw new IllegalStateException("Service is in pending booking");
        } else if (Boolean.TRUE.equals(checkServiceIsInCompletedOrCanceledBooking(id))) {
            service.setId(id);
            service.setIsDeleted(true);
            serviceRepository.save(service);
        } else if (Boolean.FALSE.equals(checkServiceIsInBooking(id))) {
            service.setId(id);
            service.setIsDeleted(true);
            service.setStatus(Status.INACTIVE);
            serviceRepository.save(service);
        }
        return "Service deleted";
    }

    @Override
    public String keepBookingAndDeleteService(UUID id) {
        var service = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));
        if (Boolean.TRUE.equals(checkServiceIsInPendingBooking(id))) {
            service.setId(id);
            service.setIsDeleted(true);
            serviceRepository.save(service);
        } else if (Boolean.TRUE.equals(checkServiceIsInCompletedOrCanceledBooking(id))) {
            throw new IllegalStateException("Service is not in pending booking");
        } else throw new IllegalStateException("Service is not in booking");
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

        Book book;

        if (writeServiceDto.getBook().getId() != null || !writeServiceDto.getBook().getId().isEmpty()) {
            book = bookRepository.findByExternalId(writeServiceDto.getBook().getId()).orElse(null);
        } else {
            book = bookRepository.findByTitle(writeServiceDto.getBook().getVolumeInfo().getTitle()).orElse(null);
        }

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
        service.setImageUrl(writeServiceDto.getImageUrl());
        service.setCreatedAt(dateUtils.getCurrentVietnamDate());
        service.setRating(0);
        service.setTotalOfBooking(0);
        service.setTotalOfReview(0);
        service.setIsDeleted(false);
        service.setStatus(Status.ACTIVE);
        return ServiceMapper.INSTANCE.toDto(serviceRepository.save(service));
    }

    @Override
    public List<ServiceDto> getListServiceByServiceTypeAndBook(UUID serviceTypeId, UUID bookId, UUID readerId) {
        ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Service type not found"));
        Book book = bookRepository.findById(bookId).orElseThrow();
        Reader reader = readerRepository.findById(readerId).orElseThrow();
        return serviceRepository.findByServiceTypeAndBook(serviceType, book, reader)
                .stream().map(ServiceMapper.INSTANCE::toDto).toList();
    }

}
