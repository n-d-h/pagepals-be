package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.dtos.service.ListService;
import com.pagepal.capstone.dtos.service.QueryDto;
import com.pagepal.capstone.mappers.ServiceMapper;
import com.pagepal.capstone.repositories.BookRepository;
import com.pagepal.capstone.repositories.ReaderRepository;
import com.pagepal.capstone.repositories.ServiceRepository;
import com.pagepal.capstone.services.ServiceProvideService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceProvideServiceImpl implements ServiceProvideService {

    private final ServiceRepository serviceRepository;
    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;

    @Override
    public ListService getAllServicesByReaderId(UUID readerId, QueryDto queryDto) {
        if(queryDto.getPage() == null || queryDto.getPage() < 0)
            queryDto.setPage(0);

        if(queryDto.getPageSize() == null || queryDto.getPageSize() < 0)
            queryDto.setPageSize(10);

        Pageable pageable;
        if(queryDto.getSort() != null && queryDto.getSort().equalsIgnoreCase("desc")){
            pageable = PageRequest.of(queryDto.getPage(), queryDto.getPageSize(), Sort.by("createdAt").descending());
        }
        else{
            pageable = PageRequest.of(queryDto.getPage(), queryDto.getPageSize(), Sort.by("createdAt").ascending());
        }

        if(queryDto.getSearch() == null){
            queryDto.setSearch("");
        }

        var reader = readerRepository.findById(readerId).orElseThrow(
                () -> new EntityNotFoundException("Reader not found")
        );

        var services = serviceRepository.findAllByReaderAndBookTitleContainsIgnoreCase(reader, queryDto.getSearch(), pageable);

        ListService list = new ListService();
        if (services == null) {
            list.setServices(Collections.emptyList());
            list.setPaging(null);
            return list;
        } else {
            PagingDto pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(services.getTotalPages());
            pagingDto.setTotalOfElements(services.getTotalElements());
            pagingDto.setSort(services.getSort().toString());
            pagingDto.setCurrentPage(services.getNumber());
            pagingDto.setPageSize(services.getSize());

            list.setServices(services.map(ServiceMapper.INSTANCE::toDto).toList());
            list.setPaging(pagingDto);
            return list;
        }
    }

    @Override
    public ListService getAllServicesByBookId(UUID bookId, QueryDto queryDto) {
        if(queryDto.getPage() == null || queryDto.getPage() < 0)
            queryDto.setPage(0);

        if(queryDto.getPageSize() == null || queryDto.getPageSize() < 0)
            queryDto.setPageSize(10);

        Pageable pageable;
        if(queryDto.getSort() != null && queryDto.getSort().equalsIgnoreCase("desc")){
            pageable = PageRequest.of(queryDto.getPage(), queryDto.getPageSize(), Sort.by("createdAt").descending());
        }
        else{
            pageable = PageRequest.of(queryDto.getPage(), queryDto.getPageSize(), Sort.by("createdAt").ascending());
        }

        if(queryDto.getSearch() == null){
            queryDto.setSearch("");
        }

        var book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException("Book not found")
        );

        var services = serviceRepository.findAllByBookId(book, queryDto.getSearch(), pageable);
        ListService list = new ListService();
        if (services == null) {
            list.setServices(Collections.emptyList());
            list.setPaging(null);
            return list;
        } else {
            PagingDto pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(services.getTotalPages());
            pagingDto.setTotalOfElements(services.getTotalElements());
            pagingDto.setSort(services.getSort().toString());
            pagingDto.setCurrentPage(services.getNumber());
            pagingDto.setPageSize(services.getSize());

            list.setServices(services.map(ServiceMapper.INSTANCE::toDto).toList());
            list.setPaging(pagingDto);
            return list;
        }
    }
}
