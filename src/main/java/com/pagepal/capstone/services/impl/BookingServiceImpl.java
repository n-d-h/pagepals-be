package com.pagepal.capstone.services.impl;

import com.pagepal.capstone.dtos.booking.*;
import com.pagepal.capstone.dtos.pagination.PagingDto;
import com.pagepal.capstone.entities.postgre.*;
import com.pagepal.capstone.enums.CurrencyEnum;
import com.pagepal.capstone.enums.MeetingEnum;
import com.pagepal.capstone.enums.TransactionStatusEnum;
import com.pagepal.capstone.enums.TransactionTypeEnum;
import com.pagepal.capstone.mappers.BookingMapper;
import com.pagepal.capstone.repositories.*;
import com.pagepal.capstone.services.BookingService;
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

import java.security.PrivateKey;
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
    private final String dollarExchangeString = "DOLLAR_EXCHANGE_RATE";
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final ReaderRepository readerRepository;
    private final DateUtils dateUtils;


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
            pageable = PageRequest.of(queryDto.getPage(), queryDto.getPageSize(), Sort.by("startAt").descending());
        } else {
            pageable = PageRequest.of(queryDto.getPage(), queryDto.getPageSize(), Sort.by("startAt").ascending());
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
            meeting = new Meeting(null, roomId, dateUtils.getCurrentVietnamDate(), 2,
                    MeetingEnum.AVAILABLE, wt.getReader(), null, null);
            meeting = meetingRepository.save(meeting);
        }
        int tokenLeft = customer.getAccount().getWallet().getTokenAmount() - bookingDto.getTotalPrice();
        if(tokenLeft < 0) {
            throw new ValidationException("Not enough token");
        }
        Booking booking = new Booking();
        booking.setCreateAt(dateUtils.getCurrentVietnamDate());
        booking.setUpdateAt(dateUtils.getCurrentVietnamDate());
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

            Transaction transaction = new Transaction();
            transaction.setStatus(TransactionStatusEnum.SUCCESS);
            transaction.setCreateAt(dateUtils.getCurrentVietnamDate());
            transaction.setTransactionType(TransactionTypeEnum.BOOKING_PAYMENT);
            transaction.setCurrency(CurrencyEnum.TOKEN);
            transaction.setBooking(res);
            transaction.setAmount(Double.valueOf(bookingDto.getTotalPrice()));
            transaction.setWallet(customer.getAccount().getWallet());
            transactionRepository.save(transaction);
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

        Transaction transaction = new Transaction();
        transaction.setStatus(TransactionStatusEnum.SUCCESS);
        transaction.setCreateAt(dateUtils.getCurrentVietnamDate());
        transaction.setTransactionType(TransactionTypeEnum.BOOKING_REFUND);
        transaction.setCurrency(CurrencyEnum.TOKEN);
        transaction.setBooking(booking);
        transaction.setAmount(Double.valueOf(booking.getTotalPrice()));
        transaction.setWallet(customer.getAccount().getWallet());
        transactionRepository.save(transaction);

        BookingState state = bookingStateRepository
                .findByName(bookingCancel)
                .orElseThrow(() -> new EntityNotFoundException("State not found"));
        booking.setState(state);
        booking.setUpdateAt(dateUtils.getCurrentVietnamDate());
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

            Transaction transaction = new Transaction();
            transaction.setStatus(TransactionStatusEnum.SUCCESS);
            transaction.setCreateAt(dateUtils.getCurrentVietnamDate());
            transaction.setTransactionType(TransactionTypeEnum.BOOKING_DONE_RECEIVE);
            transaction.setCurrency(CurrencyEnum.DOLLAR);
            transaction.setBooking(booking);
            transaction.setAmount(Double.valueOf(receiveCash));
            transaction.setWallet(booking.getService().getReader().getAccount().getWallet());
            transactionRepository.save(transaction);

            BookingState state = bookingStateRepository
                    .findByName(bookingComplete)
                    .orElseThrow(() -> new EntityNotFoundException("State not found"));
            booking.setState(state);
            booking.setUpdateAt(dateUtils.getCurrentVietnamDate());
            booking = bookingRepository.save(booking);

            var service = booking.getService();
            service.setTotalOfBooking(service.getTotalOfBooking() + 1);
            serviceRepository.save(service);

            var reader = service.getReader();
            reader.setTotalOfBookings(reader.getTotalOfBookings() + 1);
            readerRepository.save(reader);
        }

        return BookingMapper.INSTANCE.toDto(booking);
    }

    //@Secured("CUSTOMER")
    @Override
    public BookingDto reviewBooking(UUID id, ReviewBooking review) {
        if(review.getRating() < 1 || review.getRating() > 5){
            throw new ValidationException("Rating must be between 1 and 5");
        }
        Booking booking = bookingRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        if(!booking.getState().getName().equals(bookingComplete)){
            throw new ValidationException("Booking not completed yet!");
        }
        booking.setReview(review.getReview());
        booking.setRating(review.getRating());
        booking.setUpdateAt(dateUtils.getCurrentVietnamDate());
        booking = bookingRepository.save(booking);
        var service = booking.getService();
        int serviceRating = (service.getRating() * service.getTotalOfReview());
        service.setTotalOfReview(service.getTotalOfReview() + 1);
        int newServiceRating = Math.round((serviceRating + review.getRating()) / (service.getTotalOfReview()));
        service.setRating(newServiceRating);
        serviceRepository.save(service);

        var reader = service.getReader();
        int rating = (reader.getRating() * reader.getTotalOfReviews());
        reader.setTotalOfReviews(reader.getTotalOfReviews() + 1);
        int newRating = Math.round((rating + review.getRating()) / (reader.getTotalOfReviews()));
        reader.setRating(newRating);
        readerRepository.save(reader);

        return booking != null ? BookingMapper.INSTANCE.toDto(booking) : null;
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
