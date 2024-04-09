package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.googlebook.GoogleBook;
import com.pagepal.capstone.dtos.service.ServiceDto;
import com.pagepal.capstone.dtos.service.ServiceUpdate;
import com.pagepal.capstone.dtos.service.WriteServiceDto;
import com.pagepal.capstone.dtos.servicetype.ServiceTypeDto;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.ServiceMapper;
import com.pagepal.capstone.repositories.*;
import com.pagepal.capstone.services.ServiceService;
import com.pagepal.capstone.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
            book = createNewBook(writeServiceDto.getBook());
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

    private Book createNewBook(GoogleBook book) {
        Book newBook = new Book();
        List<Category> categories = new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        for (String category : book.getVolumeInfo().getCategories()) {
            Category newCategory = categoryRepository.findByName(category).orElse(null);
            if (newCategory == null) {
                newCategory = new Category();
                newCategory.setName(category);
                newCategory.setStatus(Status.ACTIVE);
                newCategory = categoryRepository.save(newCategory);
            }
            categories.add(newCategory);
        }
        for (String author : book.getVolumeInfo().getAuthors()) {
            Author newAuthor = authorRepository.findByName(author).orElse(null);
            if (newAuthor == null) {
                newAuthor = new Author();
                newAuthor.setName(author);
                newAuthor.setStatus(Status.ACTIVE);
                newAuthor = authorRepository.save(newAuthor);
            }
            authors.add(newAuthor);
        }
        newBook.setAuthors(authors);
        newBook.setCategories(categories);
        newBook.setExternalId(book.getId());
        newBook.setTitle(book.getVolumeInfo().getTitle());
        newBook.setPublisher(book.getVolumeInfo().getPublisher());
        newBook.setPublishedDate(book.getVolumeInfo().getPublishedDate());
        newBook.setDescription(book.getVolumeInfo().getDescription());
        newBook.setPageCount(book.getVolumeInfo().getPageCount());
        newBook.setThumbnailUrl(book.getVolumeInfo().getImageLinks().getThumbnail());
        newBook.setLanguage(book.getVolumeInfo().getLanguage());
        newBook.setSmallThumbnailUrl(book.getVolumeInfo().getImageLinks().getSmallThumbnail());
        newBook.setStatus(Status.ACTIVE);
        return bookRepository.save(newBook);
    }
}
