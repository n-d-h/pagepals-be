package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.dtos.seminar.ListSeminarDto;
import com.pagepal.capstone.dtos.seminar.SeminarCreateDto;
import com.pagepal.capstone.dtos.seminar.SeminarDto;
import com.pagepal.capstone.dtos.seminar.SeminarUpdateDto;
import com.pagepal.capstone.entities.postgre.Book;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.Seminar;
import com.pagepal.capstone.enums.SeminarStatus;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.SeminarMapper;
import com.pagepal.capstone.repositories.BookRepository;
import com.pagepal.capstone.repositories.ReaderRepository;
import com.pagepal.capstone.repositories.SeminarRepository;
import com.pagepal.capstone.services.BookService;
import com.pagepal.capstone.services.SeminarService;
import com.pagepal.capstone.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SeminarServiceImpl implements SeminarService {
    private final SeminarRepository seminarRepository;
    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;
    private final DateUtils dateUtils;
    private final BookService bookService;
    private static final int MAX_SEMINARS_PER_WEEK = 2;

    // Check if creating a seminar exceeds the limit
    public boolean canCreateSeminar(UUID readerId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime startOfWeek = startOfDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // convert to date
        Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Date startOfWeekDate = Date.from(startOfWeek.atZone(ZoneId.systemDefault()).toInstant());

        long seminarsThisWeek = seminarRepository.countByReaderIdAndCreatedAtBetween(readerId, startOfWeekDate, nowDate);

        return seminarsThisWeek < MAX_SEMINARS_PER_WEEK;
    }

    @Secured({"READER"})
    @Override
    public SeminarDto createSeminarRequest(SeminarCreateDto seminarCreateDto) {

        Reader reader = readerRepository.findById(seminarCreateDto.getReaderId()).orElseThrow(() -> new EntityNotFoundException("Reader not found"));

        if (Boolean.FALSE.equals(canCreateSeminar(reader.getId()))) {
            throw new ValidationException("Reader exceeds the limit of creating seminar requests");
        }

        Book book;

        if (seminarCreateDto.getBook().getId() != null || !seminarCreateDto.getBook().getId().isEmpty()) {
            book = bookRepository.findByExternalId(seminarCreateDto.getBook().getId()).orElse(null);
        }else{
            book = bookRepository.findByTitle(seminarCreateDto.getBook().getVolumeInfo().getTitle()).orElse(null);
        }

        if (book == null) {
            book = bookService.createNewBook(seminarCreateDto.getBook());
        }

        Seminar seminar = new Seminar();
        seminar.setTitle(seminarCreateDto.getTitle());
        seminar.setDescription(seminarCreateDto.getDescription());
        seminar.setImageUrl(seminarCreateDto.getImageUrl());
        seminar.setRejectReason(null);
        seminar.setDuration(seminarCreateDto.getDuration());
        seminar.setState(SeminarStatus.PENDING);
        seminar.setCreatedAt(dateUtils.getCurrentVietnamDate());
        seminar.setUpdatedAt(dateUtils.getCurrentVietnamDate());
        seminar.setReader(reader);
        seminar.setBook(book);
        seminar.setEvents(null);
        seminar.setStatus(Status.ACTIVE);

        return SeminarMapper.INSTANCE.toDto(seminarRepository.save(seminar));

    }

    @Secured({"READER"})
    @Override
    public SeminarDto updateSeminarRequest(SeminarUpdateDto seminarUpdateDto) {

        Seminar seminar = seminarRepository.findById(seminarUpdateDto.getId()).orElseThrow(() -> new EntityNotFoundException("Seminar not found"));

        if (seminar.getState() != SeminarStatus.PENDING) {
            throw new ValidationException("Seminar is not available for update");
        }

        seminar.setId(seminarUpdateDto.getId());
        seminar.setTitle(seminarUpdateDto.getTitle());
        seminar.setDescription(seminarUpdateDto.getDescription());
        seminar.setImageUrl(seminarUpdateDto.getImageUrl());
        seminar.setDuration(seminarUpdateDto.getDuration());
        seminar.setUpdatedAt(dateUtils.getCurrentVietnamDate());

        return SeminarMapper.INSTANCE.toDto(seminarRepository.save(seminar));
    }

    @Secured({"READER"})
    @Override
    public SeminarDto deleteSeminarRequest(UUID id) {
        Seminar seminar = seminarRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Seminar not found"));
        if (seminar.getState() != SeminarStatus.PENDING) {
            throw new ValidationException("Seminar is not available for delete");
        } else if (seminar.getStatus() == Status.INACTIVE) {
            throw new ValidationException("Seminar is already deleted");
        }
        seminar.setStatus(Status.INACTIVE);
        return SeminarMapper.INSTANCE.toDto(seminarRepository.save(seminar));
    }

    @Override
    public ListSeminarDto getAllSeminarRequests(Integer page, Integer pageSize, String sort) {
        if (page == null) {
            page = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        if (sort == null || sort.trim().isEmpty() || sort.trim().isBlank()) {
            sort = "asc";
        }

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").ascending());
        if (sort.equals("desc")) {
            pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        }

        Page<Seminar> list = seminarRepository.findByStatus(Status.ACTIVE, pageable);
        return getListSeminarDto(list);
    }

    @Override
    public ListSeminarDto getAllSeminarRequestsByState(Integer page, Integer pageSize, String sort, SeminarStatus state) {
        if (page == null) {
            page = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        if (sort == null || sort.trim().isEmpty() || sort.trim().isBlank()) {
            sort = "asc";
        }

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").ascending());
        if (sort.equals("desc")) {
            pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        }

        Page<Seminar> list = seminarRepository.findByStateAndStatus(state, Status.ACTIVE, pageable);
        return getListSeminarDto(list);
    }

    @Override
    public SeminarDto getSeminarRequest(UUID id) {
        var seminar = seminarRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Seminar not found"));
        if (seminar.getState() != SeminarStatus.PENDING) {
            throw new ValidationException("Seminar is not available for view");
        } else if (seminar.getStatus() == Status.INACTIVE) {
            throw new ValidationException("Seminar is already deleted");
        }
        return SeminarMapper.INSTANCE.toDto(seminar);
    }

    @Override
    public ListSeminarDto getAllSeminarRequestsByReaderId(UUID readerId, Integer page, Integer pageSize, String sort) {
        var reader = readerRepository.findById(readerId).orElseThrow(() -> new EntityNotFoundException("Reader not found"));

        if (page == null) {
            page = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        if (sort == null || sort.trim().isEmpty() || sort.trim().isBlank()) {
            sort = "asc";
        }

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").ascending());
        if (sort.equals("desc")) {
            pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        }

        Page<Seminar> list = seminarRepository.findByStatusAndReader(Status.ACTIVE, reader, pageable);
        return getListSeminarDto(list);
    }


    @Override
    public ListSeminarDto getAllSeminarRequestsByReaderIdAndState(UUID readerId, Integer page, Integer pageSize, String sort, SeminarStatus state) {
        var reader = readerRepository.findById(readerId).orElseThrow(() -> new EntityNotFoundException("Reader not found"));

        if (page == null) {
            page = 0;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        if (sort == null || sort.trim().isEmpty() || sort.trim().isBlank()) {
            sort = "asc";
        }

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").ascending());
        if (sort.equals("desc")) {
            pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        }

        Page<Seminar> list = seminarRepository.findByStateAndStatusAndReader(state, Status.ACTIVE, reader, pageable);
        return getListSeminarDto(list);
    }

    private ListSeminarDto getListSeminarDto(Page<Seminar> list) {
        ListSeminarDto listSeminarDto = new ListSeminarDto();

        PagingDto pagingDto = new PagingDto();
        pagingDto.setTotalOfPages(list.getTotalPages());
        pagingDto.setTotalOfElements(list.getTotalElements());
        pagingDto.setSort(list.getSort().toString());
        pagingDto.setCurrentPage(list.getNumber());
        pagingDto.setPageSize(list.getSize());

        // set value
        listSeminarDto.setList(list.map(SeminarMapper.INSTANCE::toDto).toList());
        listSeminarDto.setPagination(pagingDto);

        return listSeminarDto;
    }

}
