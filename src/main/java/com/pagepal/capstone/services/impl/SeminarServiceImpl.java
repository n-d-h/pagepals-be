package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.dtos.seminar.ListSeminarDto;
import com.pagepal.capstone.dtos.seminar.SeminarCreateDto;
import com.pagepal.capstone.dtos.seminar.SeminarDto;
import com.pagepal.capstone.dtos.seminar.SeminarUpdateDto;
import com.pagepal.capstone.entities.postgre.Book;
import com.pagepal.capstone.entities.postgre.Reader;
import com.pagepal.capstone.entities.postgre.Seminar;
import com.pagepal.capstone.entities.postgre.WorkingTime;
import com.pagepal.capstone.enums.SeminarStatus;
import com.pagepal.capstone.repositories.BookRepository;
import com.pagepal.capstone.repositories.BookingRepository;
import com.pagepal.capstone.repositories.ReaderRepository;
import com.pagepal.capstone.repositories.SeminarRepository;
import com.pagepal.capstone.services.SeminarService;
import com.pagepal.capstone.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeminarServiceImpl implements SeminarService {

    private final SeminarRepository seminarRepository;
    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;
    private final BookingRepository bookingRepository;
    private final DateUtils dateUtils;

    @Secured({"READER"})
    @Override
    public SeminarDto createSeminar(SeminarCreateDto seminarCreateDto) {
        try {
            Reader reader = this.checkWorkingTime(seminarCreateDto.getReaderId(), seminarCreateDto, null);
            Book book = bookRepository.findById(seminarCreateDto.getBookId()).orElse(null);
            if(book == null) {
                throw new RuntimeException("Book not found");
            }

            Seminar seminar = Seminar.builder()
                    .title(seminarCreateDto.getTitle())
                    .limitCustomer(seminarCreateDto.getLimitCustomer())
                    .activeSlot(seminarCreateDto.getActiveSlot())
                    .description(seminarCreateDto.getDescription())
                    .imageUrl(seminarCreateDto.getImageUrl())
                    .duration(seminarCreateDto.getDuration())
                    .price(seminarCreateDto.getPrice())
                    .status(SeminarStatus.ACTIVE)
                    .createdAt(dateUtils.getCurrentVietnamDate())
                    .updatedAt(dateUtils.getCurrentVietnamDate())
                    .startTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(seminarCreateDto.getStartTime()))
                    .reader(reader)
                    .book(book)
                    .build();

            var data = seminarRepository.save(seminar);
            return toSeminarDto(data);
        } catch (Exception e) {
            return null;
        }
    }

    @Secured({"READER"})
    @Override
    public SeminarDto updateSeminar(UUID readerId, UUID id, SeminarUpdateDto seminarUpdateDto) {
        try {
            Seminar seminar = seminarRepository.findById(id).orElse(null);
            if(seminar == null) {
                throw new RuntimeException("Seminar not found");
            }

            Reader reader = this.checkWorkingTime(readerId, null, seminarUpdateDto);

            Book book = bookRepository.findById(seminarUpdateDto.getBookId()).orElse(null);
            if(book == null) {
                throw new RuntimeException("Book not found");
            }

            seminar.setTitle(seminarUpdateDto.getTitle());
            seminar.setLimitCustomer(seminarUpdateDto.getLimitCustomer());
            seminar.setActiveSlot(seminarUpdateDto.getActiveSlot());
            seminar.setDescription(seminarUpdateDto.getDescription());
            seminar.setImageUrl(seminarUpdateDto.getImageUrl());
            seminar.setDuration(seminarUpdateDto.getDuration());
            seminar.setPrice(seminarUpdateDto.getPrice());
            seminar.setUpdatedAt(dateUtils.getCurrentVietnamDate());
            seminar.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(seminarUpdateDto.getStartTime()));
            seminar.setReader(reader);
            seminar.setBook(book);

            var data = seminarRepository.save(seminar);
            return toSeminarDto(data);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public SeminarDto getSeminar(UUID id) {
        var data = seminarRepository.findById(id).orElseThrow();
        return toSeminarDto(data);
    }

    @Override
    public ListSeminarDto getSeminarList(Integer page, Integer pageSize, String sort) {
        Pageable pageable = createPageable(page, pageSize, sort);
        Page<Seminar> data = seminarRepository.findAll(pageable);
        return this.mapSeminarsToDto(data);
    }

    @Override
    public ListSeminarDto getSeminarListByReaderId(UUID readerId, Integer page, Integer pageSize, String sort) {
        Pageable pageable = createPageable(page, pageSize, sort);
        Page<Seminar> data = seminarRepository.findAllByReaderId(readerId, pageable);
        return this.mapSeminarsToDto(data);
    }

    @Secured({"READER"})
    @Override
    public SeminarDto deleteSeminar(UUID id) {
        var bookings = bookingRepository.findAllBySeminarId(id);
        if(bookings.size() > 0) {
            throw new RuntimeException("Seminar has bookings");
        }
        var seminar = seminarRepository.findById(id).orElse(null);
        if(seminar == null) {
            throw new RuntimeException("Seminar not found");
        }
        seminarRepository.delete(seminar);
        return toSeminarDto(seminar);
    }

    private ListSeminarDto mapSeminarsToDto(Page<Seminar> seminars) {
        var listSeminarDto = new ListSeminarDto();

        if(seminars == null) {
            listSeminarDto.setList(Collections.emptyList());
            listSeminarDto.setPagination(null);
            return listSeminarDto;
        } else {
            var pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(seminars.getTotalPages());
            pagingDto.setTotalOfElements(seminars.getTotalElements());
            pagingDto.setSort(seminars.getSort().toString());
            pagingDto.setCurrentPage(seminars.getNumber());
            pagingDto.setPageSize(seminars.getSize());

            listSeminarDto.setList(seminars.map(this::toSeminarDto).toList());
            listSeminarDto.setPagination(pagingDto);

            return listSeminarDto;
        }
    }

    private Pageable createPageable(Integer page, Integer pageSize, String sort) {
        if (page == null || page < 0)
            page = 0;

        if (pageSize == null || pageSize < 0)
            pageSize = 10;

        if (sort != null && sort.equals("desc")) {
            return PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        } else {
            return PageRequest.of(page, pageSize, Sort.by("createdAt").ascending());
        }
    }

    private Reader checkWorkingTime(UUID readerId, SeminarCreateDto seminarCreateDto, SeminarUpdateDto seminarUpdateDto) throws Exception {
        Reader reader = readerRepository.findById(readerId).orElseThrow();
        List<WorkingTime> workingTimes = reader.getWorkingTimes();

        Date startTime = seminarCreateDto != null
                ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(seminarCreateDto.getStartTime())
                : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(seminarUpdateDto.getStartTime());
        Double duration = seminarCreateDto != null ? seminarCreateDto.getDuration() : seminarUpdateDto.getDuration();

        Date endTime = new Date(startTime.getTime() + (long) (duration * 60 * 60 * 1000));

        workingTimes.forEach(workingTime -> {
            if (startTime.after(workingTime.getStartTime()) && startTime.before(workingTime.getEndTime())) {
                throw new RuntimeException("Seminar start time is in working time");
            }

            if (endTime.after(workingTime.getStartTime()) && endTime.before(workingTime.getEndTime())) {
                throw new RuntimeException("Seminar end time is in working time");
            }
        });

        return reader;
    }

    private SeminarDto toSeminarDto(Seminar seminar) {
        return SeminarDto.builder()
                .id(seminar.getId())
                .title(seminar.getTitle())
                .limitCustomer(seminar.getLimitCustomer())
                .activeSlot(seminar.getActiveSlot())
                .description(seminar.getDescription())
                .imageUrl(seminar.getImageUrl())
                .duration(seminar.getDuration())
                .price(seminar.getPrice())
                .startTime(seminar.getStartTime())
                .status(seminar.getStatus().name())
                .createdAt(seminar.getCreatedAt())
                .updatedAt(seminar.getUpdatedAt())
                .reader(seminar.getReader())
                .book(seminar.getBook())
                .build();
    }
}
