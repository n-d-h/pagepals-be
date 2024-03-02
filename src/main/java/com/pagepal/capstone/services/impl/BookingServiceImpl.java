package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.booking.BookingCreateDto;
import com.pagepal.capstone.dtos.booking.BookingDto;
import com.pagepal.capstone.dtos.booking.ListBookingDto;
import com.pagepal.capstone.dtos.booking.QueryDto;
import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.MeetingEnum;
import com.pagepal.capstone.enums.Status;
import com.pagepal.capstone.mappers.BookingMapper;
import com.pagepal.capstone.repositories.postgre.*;
import com.pagepal.capstone.services.BookingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final ServiceRepository serviceRepository;
    private final BookingStateRepository bookingStateRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final WorkingTimeRepository workingTimeRepository;
    private final MeetingRepository meetingRepository;

    @Secured("READER")
    @Override
    public List<BookingDto> getListBookingByReader(UUID readerId) {
        var res = bookingRepository.findAllByReaderId(readerId);
        return res.stream().map(BookingMapper.INSTANCE::toDto).toList();
    }

    @Secured({"CUSTOMER", "READER"})
    @Override
    public ListBookingDto getListBookingByCustomer(UUID cusId, QueryDto queryDto) {

        if (queryDto.getPage() == null || queryDto.getPage() < 0)
            queryDto.setPage(0);
        if (queryDto.getPageSize() == null || queryDto.getPageSize() < 0)
            queryDto.setPageSize(10);

        Pageable pageable;
        if (queryDto.getSort() != null && queryDto.getSort().equals("desc")) {
            pageable = PageRequest.of(queryDto.getPage(), queryDto.getPageSize(), Sort.by("createAt").descending());
        } else {
            pageable = PageRequest.of(queryDto.getPage(), queryDto.getPageSize(), Sort.by("createAt").ascending());
        }

        Page<Booking> bookings;

        Customer customer = customerRepository
                .findById(cusId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        bookings = bookingRepository.findByCustomer(customer, pageable);

        ListBookingDto listBookingDto = new ListBookingDto();
        if (bookings == null) {
            listBookingDto.setList(Collections.emptyList());
            listBookingDto.setPagination(null);
            return listBookingDto;
        } else {
            PagingDto pagingDto = new PagingDto();
            pagingDto.setTotalOfPages(bookings.getTotalPages());
            pagingDto.setTotalOfElements(bookings.getTotalElements());
            pagingDto.setSort(bookings.getSort().toString());
            pagingDto.setCurrentPage(bookings.getNumber());
            pagingDto.setPageSize(bookings.getSize());

            listBookingDto.setList(bookings.map(BookingMapper.INSTANCE::toDto).toList());
            listBookingDto.setPagination(pagingDto);
            return listBookingDto;
        }
    }

    @Secured({"CUSTOMER", "READER"})
    @Override
    public BookingDto createBooking(UUID cusId, BookingCreateDto bookingDto) {
        Customer customer = customerRepository
                .findById(cusId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        WorkingTime wt = workingTimeRepository
                .findById(bookingDto.getWorkingTimeId())
                .orElseThrow(() -> new EntityNotFoundException("Working time not found"));
        Meeting meeting = meetingRepository
                .findByMeetingCodeAndState(bookingDto.getMeetingCode(), MeetingEnum.AVAILABLE)
                .orElse(null);
        if (meeting == null) {
            meeting = new Meeting(null, bookingDto.getMeetingCode(), new Date(), 2, MeetingEnum.AVAILABLE, wt.getReader(), null);
            meeting = meetingRepository.save(meeting);
        }
        Booking booking = new Booking();
        booking.setCreateAt(new Date());
        booking.setUpdateAt(new Date());
        booking.setDescription(bookingDto.getDescription());
        booking.setPromotionCode(bookingDto.getPromotionCode());
        booking.setCustomer(customer);
        booking.setTotalPrice(bookingDto.getTotalPrice());
        booking.setMeeting(meeting);
        booking.setWorkingTime(wt);
        booking.setStartAt(wt.getStartTime());
        booking.setState(
                bookingStateRepository
                        .findByName("PENDING")
                        .orElseThrow(() -> new EntityNotFoundException("State not found"))
        );

        Booking res = bookingRepository.save(booking);

        if (res != null) {
            UUID bookingId = res.getId();
            List<BookingDetail> listDetail = bookingDto.getBookingDetails().stream().map(bookingDetailCreateDto -> {
                BookingDetail dt = new BookingDetail();
                Chapter chapter = new Chapter();
                chapter.setId(bookingDetailCreateDto.getChapterId());
                var service = serviceRepository
                        .findByChapterAndReader(chapter, wt.getReader())
                        .orElseThrow(() -> new EntityNotFoundException("Service not found"));
                dt.setBooking(res);
                dt.setPrice(service.getPrice());
                dt.setDescription(bookingDetailCreateDto.getDescription());
                dt.setService(service);
                dt.setStatus(Status.ACTIVE);
                return dt;
            }).toList();

            listDetail = bookingDetailRepository.saveAll(listDetail);
            if (listDetail != null) {
                res.setBookingDetails(listDetail);
                return BookingMapper.INSTANCE.toDto(res);
            }
        }

        return null;
    }
}
