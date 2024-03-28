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
import jakarta.validation.ValidationException;
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
    private final SettingRepository settingRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private final String bookingCancel = "CANCEL";
    private final String bookingPending = "PENDING";
    private final String bookingComplete = "COMPLETE";
    private final String revenueString = "REVENUE_SHARE";
    private final String tokenPriceString = "TOKEN_PRICE";
    private final WalletRepository walletRepository;


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
        int tokenLeft = customer.getAccount().getWallet().getTokenAmount() - bookingDto.getTotalPrice();
        if(tokenLeft < 0) {
            throw new ValidationException("Not enough token");
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
        if(res != null){
            customer.getAccount().getWallet().setTokenAmount(tokenLeft);
            customerRepository.save(customer);
        }
        return BookingMapper.INSTANCE.toDto(res);
    }

    @Override
    public BookingDto cancelBooking(UUID id) {
        Booking booking = bookingRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        if(!booking.getState().getName().equals(bookingPending)){
            throw new ValidationException("Booking already canceled or completed!");
        }

        Customer customer = booking.getCustomer();
        int tokenLeft = customer.getAccount().getWallet().getTokenAmount() + booking.getTotalPrice();
        customer.getAccount().getWallet().setTokenAmount(tokenLeft);
        customer = customerRepository.save(customer);

        if(customer == null){
            throw new ValidationException("Cannot refund token");
        }

        BookingState state = bookingStateRepository
                .findByName(bookingCancel)
                .orElseThrow(() -> new EntityNotFoundException("State not found"));
        booking.setState(state);
        booking.setUpdateAt(new Date());
        booking = bookingRepository.save(booking);
        return BookingMapper.INSTANCE.toDto(booking);
    }

    @Override
    public BookingDto completeBooking(UUID id) {
        Booking booking = bookingRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));

        if(!booking.getState().getName().equals(bookingPending)){
            throw new ValidationException("Booking already canceled or completed!");
        }

        Setting revenue = settingRepository.findByKey(revenueString).orElse(null);
        Setting tokenPrice = settingRepository.findByKey(tokenPriceString).orElse(null);

        if(revenue == null || tokenPrice == null){
            throw new EntityNotFoundException("Setting not found");
        }

        Float receiveCash = ((booking.getTotalPrice() * Float.parseFloat(tokenPrice.getValue()))
                * (100 - Float.parseFloat(revenue.getValue())) ) / 100;
        Wallet wallet = booking.getService().getReader().getAccount().getWallet();
        wallet.setCash(wallet.getCash() + receiveCash);
        wallet = walletRepository.save(wallet);
        if(wallet != null){
            BookingState state = bookingStateRepository
                    .findByName(bookingComplete)
                    .orElseThrow(() -> new EntityNotFoundException("State not found"));
            booking.setState(state);
            booking.setUpdateAt(new Date());
            booking = bookingRepository.save(booking);
        }

        return BookingMapper.INSTANCE.toDto(booking);
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
