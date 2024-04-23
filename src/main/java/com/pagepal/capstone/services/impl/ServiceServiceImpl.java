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

    @Secured({"ADMIN", "STAFF", "READER", "CUSTOMER"})
    @Override
    public ServiceDto serviceById(UUID id) {
        return ServiceMapper.INSTANCE.toDto(serviceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Service not found")));
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

        // If service is in pending booking, we create a new service with the same information
        if (Boolean.TRUE.equals(checkServiceIsInBooking(id))) {

            // delete old service
            service.setId(id);
            service.setIsDeleted(true);
            serviceRepository.save(service);

            // create new service
            service.setId(null);
            service.setIsDeleted(false);
            service.setPrice(writeServiceDto.getPrice());
            service.setDescription(writeServiceDto.getDescription());
        } else {
            service.setId(id);
            service.setPrice(writeServiceDto.getPrice());
            service.setDescription(writeServiceDto.getDescription());
        }
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
        } else {
            service.setId(id);
            service.setIsDeleted(true);
            service.setStatus(Status.INACTIVE);
            serviceRepository.save(service);
        }
        return "Service deleted";
    }

    @Override
    public String cancelBookingAndDeleteService(UUID id) {
        var service = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));
        if (Boolean.TRUE.equals(checkServiceIsInPendingBooking(id))) {
            var pendingBookings = bookingRepository.findPendingBookingByService(id);
            for (var booking : pendingBookings) {
                bookingService.cancelBooking(booking.getId(), "Service is deleted");
            }
            service.setId(id);
            service.setIsDeleted(true);
            serviceRepository.save(service);
        } else throw new IllegalStateException("Service is not in pending booking");
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
        service.setIsDeleted(false);
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
