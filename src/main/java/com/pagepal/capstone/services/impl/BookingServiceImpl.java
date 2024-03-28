package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.booking.BookingCreateDto;
import com.pagepal.capstone.dtos.booking.BookingDto;
import com.pagepal.capstone.dtos.booking.ListBookingDto;
import com.pagepal.capstone.dtos.booking.QueryDto;
import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.MeetingEnum;
import com.pagepal.capstone.mappers.BookingMapper;
import com.pagepal.capstone.repositories.*;
import com.pagepal.capstone.services.BookingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
@Transactional
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final ServiceRepository serviceRepository;
    private final BookingStateRepository bookingStateRepository;
    private final WorkingTimeRepository workingTimeRepository;
    private final MeetingRepository meetingRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Secured("READER")
    @Override
    public ListBookingDto getListBookingByReader(UUID readerId, QueryDto queryDto) {

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

        if (queryDto.getBookingState() == null || queryDto.getBookingState().isEmpty())
            bookings = bookingRepository.findAllByReaderId(readerId, pageable);
        else {
            bookings = bookingRepository
                    .findAllByReaderIdAndBookingState(readerId,
                            queryDto.getBookingState().toUpperCase(),
                            pageable);
        }

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

        if (queryDto.getBookingState() == null || queryDto.getBookingState().isEmpty())
            bookings = bookingRepository.findByCustomer(customer, pageable);
        else {
            bookings = bookingRepository
                    .findAllByCustomerIdAndBookingState(cusId,
                            queryDto.getBookingState().toUpperCase(),
                            pageable);
        }

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
        var service = serviceRepository.findById(bookingDto.getServiceId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));
        String roomId = generateRoomId(6);
        Meeting meeting = meetingRepository
                .findByMeetingCodeAndState(roomId, MeetingEnum.AVAILABLE)
                .orElse(null);
        if (meeting == null) {
            meeting = new Meeting(null, roomId, new Date(), 2,
                    MeetingEnum.AVAILABLE, wt.getReader(), null, null);
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
        booking.setService(service);
        booking.setState(
                bookingStateRepository
                        .findByName("PENDING")
                        .orElseThrow(() -> new EntityNotFoundException("State not found"))
        );

        Booking res = bookingRepository.save(booking);

        return BookingMapper.INSTANCE.toDto(res);
    }

    private static String generateRoomId(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
